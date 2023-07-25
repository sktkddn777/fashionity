package com.infinity.fashionity.members.service;

import com.infinity.fashionity.global.utils.HashUtil;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.security.oauth.dto.AuthUserInfo;
import com.infinity.fashionity.security.oauth.dto.OAuthUserInfo;
import com.infinity.fashionity.security.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginDTO.Response login(LoginDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new MemberNotFoundException("못찾았당"));

        if (!(member.getId().equals(dto.getId()) && member.getPassword().equals(dto.getPassword()))) {
            throw new MemberNotFoundException("비번 이상하다링가링가링");
        }

        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(member.getSeq(), member.getMemberRoles()))
                .build();
    }

    @Transactional
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo) {

        // 유저가 존재하는지 확인
        MemberEntity member = memberRepository.findByEmailWithRole(oauthUserInfo.getEmail());

        if (member == null) {
            // TODO: 회원가입 로직
            MemberEntity newMember = MemberEntity.builder()
                    .id(HashUtil.makeHashId())
                    .password("password12345")
                    .nickname(nameConstraints(oauthUserInfo.getNickname()))
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

    @Override
    public SaveDTO.Response save(SaveDTO.Request dto) {
        MemberEntity member = MemberEntity.builder()
                .id(dto.getId())
                .password(dto.getPassword())
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
    private String nameConstraints(String nickname) {

        if (nickname.length() > 13) {
            nickname = nickname.trim().substring(0, 13);
        }
        return nickname;
    }
}
