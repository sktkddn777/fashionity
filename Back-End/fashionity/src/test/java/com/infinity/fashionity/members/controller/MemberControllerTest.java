package com.infinity.fashionity.members.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.members.dto.MemberFollowDTO;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;

import com.infinity.fashionity.security.service.JwtProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.infinity.fashionity.global.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;


    private final String BASE_URL = "/api/v1/members";
    private final String CONTENT_TYPE = "application/json";
    private List<MemberEntity> memberList = new ArrayList<>();
    private List<FollowEntity> memberFollowingList = new ArrayList<>();
    private List<FollowEntity> memberFollowerList = new ArrayList<>();

    @BeforeEach
    public void dummyInsert() {
        IntStream.rangeClosed(0, 10).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("testId".concat(Integer.toString(i)))
                    .password(passwordEncoder.encode("testPassword123@"))
                    .nickname("tester".concat(Integer.toString(i)))
                    .email("tester".concat(Integer.toString(i)).concat("@gmail.com"))
                    .sns(false)
                    .profileUrl("profileUrl".concat(Integer.toString(i)))
                    .profileIntro("profileIntro".concat(Integer.toString(i)))
                    .gender(Gender.MALE)
                    .height(178.0f)
                    .weight(73.0f)
                    .build();

            member.getMemberRoles().add(MemberRoleEntity.builder()
                    .member(member)
                    .memberRole(MemberRole.USER)
                    .build());

            memberList.add(member);

            if (i > 1) {
                MemberEntity followingOrFollowedMember = memberList.get(i-1);
                if (i % 2 == 0) {
                    memberFollowingList.add(FollowEntity.builder()
                            .member(member)
                            .followedMember(followingOrFollowedMember)
                            .build());
                } else {
                    memberFollowerList.add(FollowEntity.builder()
                            .member(followingOrFollowedMember)
                            .followedMember(member)
                            .build());
                }
            }
        });
        memberRepository.saveAll(memberList);
        followRepository.saveAll(memberFollowingList);
        followRepository.saveAll(memberFollowerList);
    }

    public LoginDTO.Response memberLogin(Long seq) throws Exception {
        List<MemberRoleEntity> roles = new ArrayList<>();
        roles.add(MemberRoleEntity.builder()
                .member(MemberEntity.builder().seq(seq).build())
                .memberRole(MemberRole.USER)
                .build());

        return LoginDTO.Response.builder()
                .accessToken(jwtProvider.createAccessToken(seq, roles))
                .build();
    }

    @Nested
    @DisplayName("Profile Test")
    public class ProfileTest {

        // accessToken
        String accessToken;

        // 현재 사용자
        Long memberSeq;

        // 프로필 사용자
        Long profileMemberSeq;

        @BeforeEach
        public void init() throws Exception {
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();

            memberSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            profileMemberSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            accessToken = memberLogin(memberSeq).getAccessToken();
        }

        @Test
        @DisplayName("get member profile")
        public void getMemberProfileTest() throws Exception {

            // given
            MemberEntity profileMember = memberRepository.findById(profileMemberSeq).get();
            String profileMemberNickname = profileMember.getNickname();
            MemberEntity member = memberRepository.findById(memberSeq).get();

            // when & then
            mvc.perform(get(BASE_URL.concat("/{nickname}"), profileMemberNickname)
                    .contentType(CONTENT_TYPE)
                    .characterEncoding(UTF_8)
                    .header("Authorization", "Bearer ".concat(accessToken)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("getMemberProfile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.profileUrl", is(profileMember.getProfileUrl())))
                    .andExpect(jsonPath("$.nickname", is(profileMemberNickname)))
                    .andExpect(jsonPath("$.profileIntro", is(profileMember.getProfileIntro())))
                    .andExpect(jsonPath("$.followerCnt", is(followRepository.findByFollowedMember(profileMember).size())))
                    .andExpect(jsonPath("$.followingCnt", is(followRepository.findByMember(profileMember).size())))
                    .andExpect(jsonPath("$.myProfile", is(memberSeq == profileMemberSeq)));

        }

        @Test
        @DisplayName("edit my profile success")
        public void editMyProfileSuccessTest() throws Exception {

            // given
            MemberEntity member = memberRepository.findById(memberSeq).get();
            ProfileDTO.Request request = ProfileDTO.Request.builder()
                    .profileUrl("newProfileUrl")
                    .profileIntro("newProfileIntro")
                    .nickname("newNickname001")
                    .build();

            // when & then
            mvc.perform(patch(BASE_URL.concat("/edit"))
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("editMyProfile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.profileUrl", is(request.getProfileUrl())))
                    .andExpect(jsonPath("$.nickname", is(request.getNickname())))
                    .andExpect(jsonPath("$.profileIntro", is(request.getProfileIntro())))
                    .andExpect(jsonPath("$.followerCnt", is(followRepository.findByFollowedMember(member).size())))
                    .andExpect(jsonPath("$.followingCnt", is(followRepository.findByMember(member).size())))
                    .andExpect(jsonPath("$.myProfile", is(true)));

            MemberEntity updatedMember = memberRepository.findById(memberSeq).get();
            assertThat(updatedMember.getNickname()).isEqualTo(request.getNickname());
            assertThat(updatedMember.getProfileIntro()).isEqualTo(request.getProfileIntro());
            assertThat(updatedMember.getProfileUrl()).isEqualTo(request.getProfileUrl());

        }

        @Test
        @DisplayName("edit my profile fail")
        public void editMyProfileFailTest() throws Exception {

            // given
            // 닉네임 제약조건에 맞지 않은 케이스
            MemberEntity member = memberRepository.findById(memberSeq).get();
            ProfileDTO.Request request = ProfileDTO.Request.builder()
                    .profileUrl("newProfileUrl")
                    .profileIntro("newProfileIntro")
                    .nickname("invalid")
                    .build();

            // when & then
            mvc.perform(patch(BASE_URL.concat("/edit"))
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("editMyProfile"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_NICKNAME.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_NICKNAME.getMessage())));

            MemberEntity updatedFailMember = memberRepository.findById(memberSeq).get();
            assertThat(updatedFailMember.getNickname()).isEqualTo(member.getNickname());
            assertThat(updatedFailMember.getProfileIntro()).isEqualTo(member.getProfileIntro());
            assertThat(updatedFailMember.getProfileUrl()).isEqualTo(member.getProfileUrl());

        }
        @Test
        @DisplayName("edit my password success")
        public void editMyPasswordSuccessTest() throws Exception {

            // given
            MemberEntity member = memberRepository.findById(memberSeq).get();
            ProfileDTO.PwRequest request = ProfileDTO.PwRequest.builder()
                    .password("testPassword123@")
                    .newPassword("newPassword123@")
                    .build();

            // when & then
            mvc.perform(patch(BASE_URL.concat("/edit/pw"))
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("editMyPassword"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)));

            MemberEntity updatedMember = memberRepository.findById(memberSeq).get();
            assertThat(passwordEncoder.matches("newPassword123@", updatedMember.getPassword())).isTrue();
        }

        @Test
        @DisplayName("edit my password fail credential not match")
        public void editMyPasswordCredentialNotMatchFailTest() throws Exception {

            // given
            MemberEntity member = memberRepository.findById(memberSeq).get();
            ProfileDTO.PwRequest request = ProfileDTO.PwRequest.builder()
                    .password("invalidPassword123@")
                    .newPassword("newPassword123@")
                    .build();

            // when & then
            mvc.perform(patch(BASE_URL.concat("/edit/pw"))
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("editMyPassword"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code", is(CREDENTIAL_NOT_MATCHED.getCode())))
                    .andExpect(jsonPath("$.message", is(CREDENTIAL_NOT_MATCHED.getMessage())));
        }

        @Test
        @DisplayName("edit my password fail invalid new password")
        public void editMyPasswordInvalidNewPasswordFailTest() throws Exception {

            // given
            MemberEntity member = memberRepository.findById(memberSeq).get();
            ProfileDTO.PwRequest request = ProfileDTO.PwRequest.builder()
                    .password("testPassword123@")
                    .newPassword("invalid")
                    .build();

            // when & then
            mvc.perform(patch(BASE_URL.concat("/edit/pw"))
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("editMyPassword"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_PASSWORD.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_PASSWORD.getMessage())));
        }
    }

    @Nested
    @DisplayName("Follow List Test")
    public class FollowListTest {

        // accessToken
        String accessToken;

        // 현재 사용자
        Long memberSeq;

        // 팔로잉, 팔로워를 보여주는 사용자
        Long profileMemberSeq;

        @BeforeEach
        public void init() throws Exception {
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();

            memberSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            profileMemberSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            accessToken = memberLogin(memberSeq).getAccessToken();
        }

        @Test
        @DisplayName("get following list")
        public void getFollowingListTest() throws Exception {

            // given
            MemberEntity profileMember = memberRepository.findById(profileMemberSeq).get();
            List<FollowEntity> byMember = followRepository.findByMember(profileMember);

            // when & then
            MvcResult mvcResult = mvc.perform(get(BASE_URL.concat("/{nickname}/followings"), profileMember.getNickname())
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("getFollowings"))
                    .andExpect(status().isOk())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString(UTF_8);
            MemberFollowDTO.FollowingResponse response = objectMapper.readValue(content, MemberFollowDTO.FollowingResponse.class);

            assertThat(response.getFollowings().size()).isEqualTo(byMember.size());
        }

        @Test
        @DisplayName("get follower list")
        public void getFollowerListTest() throws Exception {

            // given
            MemberEntity profileMember = memberRepository.findById(profileMemberSeq).get();
            List<FollowEntity> byFollowedMember = followRepository.findByFollowedMember(profileMember);

            // when & then
            MvcResult mvcResult = mvc.perform(get(BASE_URL.concat("/{nickname}/followers"), profileMember.getNickname())
                            .contentType(CONTENT_TYPE)
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("getFollowers"))
                    .andExpect(status().isOk())
                    .andReturn();

            String content = mvcResult.getResponse().getContentAsString(UTF_8);
            MemberFollowDTO.FollowerResponse response = objectMapper.readValue(content, MemberFollowDTO.FollowerResponse.class);

            assertThat(response.getFollowers().size()).isEqualTo(byFollowedMember.size());
        }
    }

}