package teamproject.lam_server.domain.member.dto.request;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class LoginRequest {

    @NotBlank
    @Pattern(regexp = "[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*")
    private String email;

    @NotBlank
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(
                email, password
        );
    }
}
