package com.infinity.fashionity.follows.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.follows.dto.FollowDTO;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.security.service.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static com.infinity.fashionity.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class FollowControllerTest {

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

    private final String BASE_URL = "/api/v1/follows";
    private final String CONTENT_TYPE = "application/json";

    private List<MemberEntity> memberList = new ArrayList<>();


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
        });

        memberRepository.saveAll(memberList);
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
    @DisplayName("Follow Test")
    public class FollowTest {

        // follow 하는 사람
        Long followSeq;

        // follow 당하는 사람
        Long followedSeq;

        // accessToken
        String accessToken;

        @BeforeEach
        public void init() throws Exception {
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();

            followSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            followedSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            accessToken = memberLogin(followSeq).getAccessToken();
        }

        @Test
        @DisplayName("follow success")
        public void followSuccessTest() throws Exception {

            // given
            Optional<MemberEntity> followedMember = memberRepository.findById(followedSeq);
            FollowDTO.Request request = FollowDTO.Request.builder()
                    .nickname(followedMember.get().getNickname())
                    .build();

            FollowKey followKey = FollowKey.builder()
                    .member(followSeq)
                    .followedMember(followedSeq)
                    .build();

            // when & then
            MvcResult result = mvc.perform(post(BASE_URL)
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(FollowController.class))
                    .andExpect(handler().methodName("follow"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andReturn();


            Optional<FollowEntity> byFollowKey = followRepository.findById(followKey);
            assertThat(byFollowKey).isNotEmpty();
        }

        @Test
        @DisplayName("followedMember Not Found")
        public void followedMemberNotFoundTest() throws Exception {

            // given
            String nickname = "notExistNickName";

            Optional<MemberEntity> followedMember = memberRepository.findById(followedSeq);
            FollowDTO.Request request = FollowDTO.Request.builder()
                    .nickname(nickname)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL)
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(FollowController.class))
                    .andExpect(handler().methodName("follow"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code", is(MEMBER_NOT_FOUND.getCode())))
                    .andExpect(jsonPath("$.message", is(MEMBER_NOT_FOUND.getMessage())));

            Optional<MemberEntity> byNickname = memberRepository.findByNickname(nickname);
            assertThat(byNickname).isEmpty();
        }

        @Test
        @DisplayName("already follow")
        public void AlreadyFollowTest() throws Exception {

            // given
            Optional<MemberEntity> followedMember = memberRepository.findById(followedSeq);
            Optional<MemberEntity> followMember = memberRepository.findById(followSeq);

            FollowDTO.Request request = FollowDTO.Request.builder()
                    .nickname(followedMember.get().getNickname())
                    .build();

            FollowEntity followEntity = FollowEntity.builder()
                    .member(followMember.get())
                    .followedMember(followedMember.get())
                    .build();

            FollowKey followKey = FollowKey.builder()
                    .member(followSeq)
                    .followedMember(followedSeq)
                    .build();

            followRepository.save(followEntity);
            Optional<FollowEntity> test = followRepository.findById(followKey);
            System.out.println("hello:" + test.get().getMember().toString());
            System.out.println("hello2:" + test.get().getFollowedMember().toString());

            // when & then
            mvc.perform(post(BASE_URL)
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(FollowController.class))
                    .andExpect(handler().methodName("follow"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(ALREADY_FOLLOW.getCode())))
                    .andExpect(jsonPath("$.message", is(ALREADY_FOLLOW.getMessage())));

            Optional<FollowEntity> byId = followRepository.findById(followKey);
            assertThat(byId).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("unFollow Test")
    public class UnFollowTest {

        // follow 하는 사람
        Long followSeq;

        // follow 당하는 사람
        Long followedSeq;

        // accessToken
        String accessToken;

        @BeforeEach
        public void init() throws Exception {
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();

            followSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            followedSeq = memberList.get((int) (Math.abs(random.nextLong()) % totalMemberSize)).getSeq();
            accessToken = memberLogin(followSeq).getAccessToken();
        }

        @Test
        @DisplayName("unFollow success")
        public void unFollowSuccessTest() throws Exception {

            // given
            Optional<MemberEntity> followedMember = memberRepository.findById(followedSeq);
            Optional<MemberEntity> followMember = memberRepository.findById(followSeq);

            FollowDTO.Request request = FollowDTO.Request.builder()
                    .nickname(followedMember.get().getNickname())
                    .build();

            FollowEntity followEntity = FollowEntity.builder()
                    .member(followMember.get())
                    .followedMember(followedMember.get())
                    .build();

            FollowKey followKey = FollowKey.builder()
                    .member(followSeq)
                    .followedMember(followedSeq)
                    .build();

            followRepository.save(followEntity);

            // when & then
            MvcResult result = mvc.perform(delete(BASE_URL)
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(FollowController.class))
                    .andExpect(handler().methodName("unFollow"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andReturn();

            Optional<FollowEntity> byFollowKey = followRepository.findById(followKey);
            assertThat(byFollowKey).isEmpty();
        }

        @Test
        @DisplayName("already unfollow")
        public void AlreadyUnfollowTest() throws Exception {

            // given
            Optional<MemberEntity> followedMember = memberRepository.findById(followedSeq);
            Optional<MemberEntity> followMember = memberRepository.findById(followSeq);

            FollowDTO.Request request = FollowDTO.Request.builder()
                    .nickname(followedMember.get().getNickname())
                    .build();

            FollowKey followKey = FollowKey.builder()
                    .member(followSeq)
                    .followedMember(followedSeq)
                    .build();

            // when & then
            MvcResult result = mvc.perform(delete(BASE_URL)
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .header("Authorization", "Bearer ".concat(accessToken))
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(FollowController.class))
                    .andExpect(handler().methodName("unFollow"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(NOT_FOLLOW.getCode())))
                    .andExpect(jsonPath("$.message", is(NOT_FOLLOW.getMessage())))
                    .andReturn();

            Optional<FollowEntity> byFollowKey = followRepository.findById(followKey);
            assertThat(byFollowKey).isEmpty();
        }
    }

}