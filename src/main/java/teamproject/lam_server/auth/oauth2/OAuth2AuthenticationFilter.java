package teamproject.lam_server.auth.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import teamproject.lam_server.auth.dto.CustomOAuth2AuthenticationToken;
import teamproject.lam_server.auth.dto.OAuth2TokenRequest;
import teamproject.lam_server.auth.dto.OAuth2TokenResponse;
import teamproject.lam_server.util.OAuth2Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;


@Component
@Slf4j
public class OAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String DEFAULT_OAUTH2_LOGIN_PATH_PREFIX = "/oauth2/login/*";
    private static final String HTTP_METHOD = "GET";
    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX + "*", HTTP_METHOD);

    private final ClientRegistrationRepository clientRegistrationRepository;

    public OAuth2AuthenticationFilter(OAuth2AuthenticationProvider oAuth2AuthenticationProvider,   //Provider??? ??????????????????. ?????? ?????? ?????? ???????????????.
                                      OAuth2SuccessHandler successHandler,  //????????? ?????? ??? ?????????  handler??????
                                      OAuth2FailureHandler failureHandler,
                                      ClientRegistrationRepository clientRegistrationRepository) { //????????? ?????? ??? ????????? handler??????.

        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);   // ????????? ?????????  /oauth2/login/* ??? ?????????, GET?????? ??? ????????? ???????????? ?????? ????????????.

        this.setAuthenticationManager(new ProviderManager(oAuth2AuthenticationProvider));

        this.setAuthenticationSuccessHandler(successHandler);
        this.setAuthenticationFailureHandler(failureHandler);
        this.clientRegistrationRepository = clientRegistrationRepository;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // request?????? ???????????? ??????(code, state, error)??? ?????????.
        MultiValueMap<String, String> params = OAuth2Util.toMultiMap(request.getParameterMap());
        // ???????????? ??????.
        if (!OAuth2Util.isAuthorizationResponse(params)) {
            OAuth2Error oauth2Error = new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        }
        // registration ?????? ????????????
        String registrationId = this.extractRegistrationId(request);
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId(registrationId);

        // ????????? ?????? ?????? ????????? ?????? request ?????? ??????
        OAuth2TokenRequest tokenRequest = OAuth2TokenRequest.of(clientRegistration, params);

        // ????????? ????????? uri??? request??? ????????? ????????????(reponse)??? ??????
        String tokenUri = clientRegistration.getProviderDetails().getTokenUri();
        OAuth2TokenResponse tokenResponse = getOAuth2AccessToken(tokenUri, tokenRequest);

        Authentication authenticate = this.getAuthenticationManager().authenticate(
                new CustomOAuth2AuthenticationToken(clientRegistration, tokenResponse.getAccessToken()));

        return authenticate;
    }

    private String extractRegistrationId(HttpServletRequest request) {
        return request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_PATH_PREFIX.length() - 1);
    }

    private OAuth2TokenResponse getOAuth2AccessToken(String tokenUri, OAuth2TokenRequest tokenRequest) {
        return WebClient.create()
                .post()
                .uri(tokenUri)
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest.toMultiMap())
                .retrieve()
                .bodyToMono(OAuth2TokenResponse.class)
                .block();
    }
}
