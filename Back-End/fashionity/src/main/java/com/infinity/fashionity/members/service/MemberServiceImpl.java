package com.infinity.fashionity.members.service;

import com.infinity.fashionity.global.utils.HashUtil;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.exception.IdOrPasswordNotMatchedException;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import com.infinity.fashionity.security.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.util.Password;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

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
                .orElseThrow(MemberNotFoundException::new);

        if (!member.getId().equals(dto.getId()) || !passwordEncoder.matches(dto.getPassword(),member.getPassword()))
            throw new IdOrPasswordNotMatchedException();

        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(member.getSeq(), member.getMemberRoles()))
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
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {

        // 유저가 존재하는지 확인
        MemberEntity member = memberRepository.findByEmailWithRole(oauthUserInfo.getEmail());

        if (member == null) {
            // TODO: 회원가입 로직
            MemberEntity newMember = MemberEntity.builder()
                    .id(HashUtil.makeHashId())
                    .password(passwordEncoder.encode("password12345"))
                    .nickname(oAuthNameConstraints(oauthUserInfo.getNickname()))
                    .email(oauthUserInfo.getEmail())
                    .profileUrl(oauthUserInfo.getProfileImgUrl())
                    .sns(true)
                    .snsType(SNSType.GOOGLE)
                    .build();

            MemberRoleEntity memberRole = MemberRoleEntity.builder()
                    .memberRole(MemberRole.USER)
                    .member(newMember)
                    .build();

            newMember.getMemberRoles().add(memberRole);

            member = memberRepository.save(newMember);
        }

        List<MemberRoleEntity> memberRoles = member.getMemberRoles();
        return new AuthUserInfo(member.getSeq(), member.getEmail(), memberRoles);
    }

    /**
     * 일반 로그인 전용 회원가입
     * 처음 가입하는 멤버는 role: USER 로 통일
     * @param dto
     * @return
     */
    @Override
    public SaveDTO.Response register(SaveDTO.Request dto) {

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
                .nickname(savedMember.getNickname())
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
}
