package teamproject.lam_server.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamproject.lam_server.domain.member.dto.request.FindIdRequest;
import teamproject.lam_server.domain.member.dto.request.FindPasswordRequest;
import teamproject.lam_server.domain.member.dto.request.ModifyMemberRequest;
import teamproject.lam_server.domain.member.dto.request.SignUpRequest;
import teamproject.lam_server.domain.member.dto.response.DuplicateCheckResponse;
import teamproject.lam_server.domain.member.dto.response.FindIdResponse;
import teamproject.lam_server.domain.member.entity.Member;
import teamproject.lam_server.domain.member.repository.MemberCheckRepository;
import teamproject.lam_server.domain.member.repository.MemberRepository;
import teamproject.lam_server.exception.badrequest.NotDropMember;
import teamproject.lam_server.exception.notfound.MemberNotFound;
import teamproject.lam_server.global.dto.PostIdResponse;
import teamproject.lam_server.mail.dto.TempPasswordSendMailInfo;
import teamproject.lam_server.mail.service.MailService;
import teamproject.lam_server.util.JwtUtil;

import static teamproject.lam_server.global.constants.ResponseMessage.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberCheckRepository memberCheckRepository;
    private final MailService mailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PostIdResponse signUp(SignUpRequest request) {
        Member saveMember = memberRepository.save(request.toEntity(passwordEncoder));
        return PostIdResponse.of(saveMember.getId());
    }

    @Override
    public DuplicateCheckResponse checkDuplicateEmail(String email) {
        Boolean isDuplicated = memberCheckRepository.existsByEmail(email);
        return isDuplicated
                ? DuplicateCheckResponse.of(false, email, DUPLICATE_EMAIL)
                : DuplicateCheckResponse.of(true, email, AVAILABLE_EMAIL);
    }

    @Override
    public DuplicateCheckResponse checkDuplicateLoginId(String LoginId) {
        Boolean isDuplicated = memberCheckRepository.existsByLoginId(LoginId);
        return isDuplicated
                ? DuplicateCheckResponse.of(false, LoginId, DUPLICATE_LOGIN_ID)
                : DuplicateCheckResponse.of(true, LoginId, AVAILABLE_LOGIN_ID);
    }

    @Override
    public DuplicateCheckResponse checkDuplicateNickname(String nickname) {
        Boolean isDuplicated = memberCheckRepository.existsByEmail(nickname);
        return isDuplicated
                ? DuplicateCheckResponse.of(false, nickname, DUPLICATE_NICKNAME)
                : DuplicateCheckResponse.of(true, nickname, AVAILABLE_NICKNAME);
    }

    @Override
    public FindIdResponse findLoginId(FindIdRequest request) {
        Member findMember = memberRepository.findByNameAndEmail(
                        request.getName(), request.getEmail())
                .orElseThrow(MemberNotFound::new);
        return FindIdResponse.of(findMember);
    }

    @Override
    @Transactional
    public void findPassword(FindPasswordRequest request) {
        // find user with request
        Member findMember = memberRepository.findByLoginIdAndEmail(request.getLoginId(), request.getEmail())
                .orElseThrow(MemberNotFound::new);

        // create random password
        String tempPassword = JwtUtil.createRandomPassword();

        // update password (temporary password)
        findMember.updatePassword(passwordEncoder.encode(tempPassword));

        // mail send
        mailService.sendMail(TempPasswordSendMailInfo.of(findMember));
    }

    @Override
    @Transactional
    public void modify(Long id, ModifyMemberRequest request) {
        Member member = memberRepository.getById(id);
        member.modifyMemberInfo(request.getNickname(), request.getImage());
    }

    @Override
    @Transactional
    public void dropUser(Long id) {
        Member dropMember = memberRepository.getById(id);
        dropMember.drop();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Long queryCount = memberRepository.cleanDeleteById(id);
        if (queryCount == 0) throw new NotDropMember();
    }
}
