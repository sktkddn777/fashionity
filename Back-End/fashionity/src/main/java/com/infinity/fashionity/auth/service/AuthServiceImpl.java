package com.infinity.fashionity.auth.service;

import com.infinity.fashionity.auth.dto.*;
import com.infinity.fashionity.auth.exception.MailSendException;
import com.infinity.fashionity.global.utils.HashUtil;
import com.infinity.fashionity.global.utils.JwtUtil;
import com.infinity.fashionity.global.utils.RegexUtil;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.exception.AlreadyExistException;
import com.infinity.fashionity.members.exception.CustomValidationException;
import com.infinity.fashionity.members.exception.IdOrPasswordNotMatchedException;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import com.infinity.fashionity.security.service.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.infinity.fashionity.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final JwtUtil jwtUtil;

    /**
     * 일반 로그인 전용
     * 아이디가 없으면 MemberNotFoundException
     * 로그인 정보가 틀리면 IdOrPasswordNotMatchedException
     * @param dto
     * @return 유저 seq + 유저 role 담은 accessToken 반환
     */
    @Override
    public LoginDTO.Response login(LoginDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        if (!member.getId().equals(dto.getId()) || !passwordEncoder.matches(dto.getPassword(),member.getPassword()))
            throw new IdOrPasswordNotMatchedException(CREDENTIAL_NOT_MATCHED);

        String refreshToken = jwtProvider.createRefreshToken();

        List<MemberRole> roles = new ArrayList<>();
        MemberEntity byEmailWithRole = memberRepository.findByEmailWithRole(member.getEmail());
        for (MemberRoleEntity mr : byEmailWithRole.getMemberRoles())
            roles.add(mr.getMemberRole());

        return LoginDTO.Response.builder()
                .memberSeq(member.getSeq())
                .profileUri(member.getProfileUrl())
                .nickname(member.getNickname())
                .memberRole(roles)
                .accessToken(jwtProvider.createAccessToken(member.getSeq(), member.getMemberRoles()))
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 소셜 로그인 전용 로그인 및 회원가입
     * 이미 가입된 유저라면 DB 에 있는 유저 반환
     * 새로 가입하는 유저라면 (  ) 어떻게 할지 생각
     * @param oauthUserInfo
     * @return
     */
    @Override
    @Transactional
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {

        // 유저가 존재하는지 확인
        MemberEntity member = memberRepository.findByEmailWithRole(oauthUserInfo.getEmail());

        if (member == null) {
            MemberEntity newMember = MemberEntity.builder()
                    .id(HashUtil.makeHashId())
                    .password(passwordEncoder.encode("password12345"))
                    .nickname(oAuthNameConstraints(oauthUserInfo.getNickname()))
                    .email(oauthUserInfo.getEmail())
                    .profileUrl(oauthUserInfo.getProfileImgUrl())
                    .sns(true)
                    .snsType(SNSType.valueOf(oauthUserInfo.getProvider().toUpperCase()))
                    .build();

            MemberRoleEntity memberRole = MemberRoleEntity.builder()
                    .memberRole(MemberRole.USER)
                    .member(newMember)
                    .build();

            newMember.getMemberRoles().add(memberRole);

            member = memberRepository.save(newMember);
        }

        return new AuthUserInfo(member.getSeq(), member.getEmail(), member.getMemberRoles());
    }

    private void registerDtoValidation(SaveDTO.Request dto) {
        if (!RegexUtil.checkIdRegex(dto.getId()))
            throw new CustomValidationException(INVALID_MEMBER_ID);
        if (!RegexUtil.checkNicknameRegex(dto.getNickname()))
            throw new CustomValidationException(INVALID_MEMBER_NICKNAME);
        if (!RegexUtil.checkEmailRegex(dto.getEmail()))
            throw new CustomValidationException(INVALID_MEMBER_EMAIL);
        if (!RegexUtil.checkPasswordRegex(dto.getPassword()))
            throw new CustomValidationException(INVALID_MEMBER_PASSWORD);
    }

    /**
     * 일반 로그인 전용 회원가입
     * 처음 가입하는 멤버는 role: USER 로 통일
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public SaveDTO.Response register(SaveDTO.Request dto) {

        // 이메일, 아이디, 닉네임 중복검사
        Optional<MemberEntity> byId = memberRepository.findById(dto.getId());
        if (byId.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_ID);

        Optional<MemberEntity> byEmail = memberRepository.findByEmail(dto.getEmail());
        if (byEmail.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_EMAIL);

        Optional<MemberEntity> byNickname = memberRepository.findByNickname(dto.getNickname());
        if (byNickname.isPresent())
            throw new AlreadyExistException(EXIST_MEMBER_NICKNAME);

        // 정규식을 통해 유효성 검사
        registerDtoValidation(dto);

        MemberEntity member = MemberEntity.builder()
                .id(dto.getId())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .sns(dto.getSns())
                .build();

        MemberRoleEntity memberRole = MemberRoleEntity.builder()
                .memberRole(MemberRole.USER)
                .member(member)
                .build();

        member.getMemberRoles().add(memberRole);

        MemberEntity savedMember = memberRepository.save(member);

        return SaveDTO.Response.builder()
                .id(savedMember.getId())
                .email(savedMember.getEmail())
                .build();
    }

    @Override
    public boolean isIdValidate(String id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (byId.isPresent())
            return false;
        return true;
    }

    @Override
    public boolean isNicknameValidate(String nickname) {
        Optional<MemberEntity> byNickname = memberRepository.findByNickname(nickname);
        if (byNickname.isPresent())
            return false;
        return true;
    }

    @Override
    public boolean isEmailValidate(String email) {
        Optional<MemberEntity> byEmail = memberRepository.findByEmail(email);
        if (byEmail.isPresent())
            return false;
        return true;
    }

    @Override
    public FindByEmailDTO.IDResponse findIdByEmail(FindByEmailDTO.IDRequest dto) {
        try {
            MemberEntity member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new MemberNotFoundException(MEMBER_NOT_FOUND)
            );

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setTo(dto.getEmail());
            mimeMessageHelper.setSubject("[FASHIONITY] 찾으시는 아이디입니다.");
            mimeMessageHelper.setFrom("bsrg@fashionity.com");
            mimeMessageHelper.setText(createMailForm("찾으시는 아이디", member.getId()), true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("[ERROR] MessagingException = {}, {}", e.getClass().getSimpleName(), e.getMessage());
            throw new MailSendException(EMAIL_ERROR);
        }
        return FindByEmailDTO.IDResponse.builder()
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public FindByEmailDTO.PasswordResponse reissuePasswordByEmail(FindByEmailDTO.PasswordRequest dto) {
        try {
            MemberEntity member = memberRepository.findByIdAndEmail(dto.getId(), dto.getEmail()).orElseThrow(() ->
                new MemberNotFoundException(MEMBER_NOT_FOUND)
            );

            String newPassword = HashUtil.makeHashPassword();
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessageHelper.setTo(dto.getEmail());
            mimeMessageHelper.setSubject("[FASHIONITY] 임시 비밀번호 재발급 메일입니다.");
            mimeMessageHelper.setFrom("bsrg@fashionity.com");
            mimeMessageHelper.setText(createMailForm("임시 비밀번호 재발급", newPassword), true);

            member.setPassword(passwordEncoder.encode(newPassword));
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("[ERROR] MessagingException = {}, {}", e.getClass().getSimpleName(), e.getMessage());
            throw new MailSendException(EMAIL_ERROR);
        }

        return FindByEmailDTO.PasswordResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public LogoutDTO.Response logout() {

        // 컨텍스트에 있는 값 제거
        SecurityContextHolder.clearContext();
        return LogoutDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    public ReissueDTO.Response reissue(String refreshToken, String accessToken, Long memberSeq) {

        String newAccessToken = accessToken;
        // AT 만료 여부 확인
        try {
            log.info("AT 만료 여부 확인");
            jwtUtil.validateToken(accessToken);
        } catch (Exception e) {
            log.info("RT 만료 여부 확인");
            // RT 유효성 확인 (만료 여부, 변조 여부)
            jwtUtil.validateToken(refreshToken);

            MemberEntity member = memberRepository.findById(memberSeq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
            newAccessToken = jwtProvider.createAccessToken(member.getSeq(), member.getMemberRoles());
        }

        return ReissueDTO.Response.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }


    /**
     * 소셜로그인으로 받아온 닉네임 길이 13자 이상일 때 13자로 제한
     * @param nickname 소셜 로그인으로 받아온 이름
     * @return nickname 제약 조건에 만족한 이름
     */
    private String oAuthNameConstraints(String nickname) {
        if (nickname.length() > 13) {
            nickname = nickname.trim().substring(0, 13);
        }
        return nickname;
    }

    private String createMailForm(String purpose, String value) {
        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1> 안녕하세요, FASHIONITY 입니다. </h1>";
        msg += "<br>";
        msg += "<p>고객님이 요청하신 " + purpose + " 입니다. <p>";
        msg += "<br>";
        msg += "<p>감사합니다!<p>";
        msg += "<br>";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'> " + purpose + " 입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "<strong>";
        msg += value + "</strong><div><br/> ";
        msg += "</div>";
        return msg;
    }
}
