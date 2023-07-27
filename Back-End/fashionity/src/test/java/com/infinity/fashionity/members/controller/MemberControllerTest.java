package com.infinity.fashionity.members.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.dto.SaveDTO;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.members.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.infinity.fashionity.global.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/api/v1/members";
    private List<MemberEntity> memberList = new ArrayList<>();


    @BeforeEach
    public void dummyInsert() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
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

    @Nested
    @DisplayName("Member Register Test")
    public class MemberRegisterTest {

        /**
         * 회원가입 제약조건
         * 아이디: 6 ~ 20 + 영어, 숫자만 가능,
         * 비밀번호: 5 ~ 60 + 영어, 숫자, 특수문자 혼용,
         * 닉네임: () +
         * 이메일: ()
         */

        private final String REGISTER_REQUEST_URL = "/register";

        @Test
        @DisplayName("회원가입 성공")
        public void memberRegisterSuccessTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("sangwoo123")
                    .email("sktkddn777@gmail.com")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(request.getId())))
                    .andExpect(jsonPath("$.email", is(request.getEmail())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isNotEmpty()
            );
        }

        @Test
        @DisplayName("아이디가 제약 조건에 맞지 않는 오류")
        public void invalidIdTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("sangwoo123")
                    .email("sktkddn777@gmail.com")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_ID.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_ID.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }

        @Test
        @DisplayName("비밀번호가 제약조건에 맞지 않는 오류")
        public void invalidPasswordTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("sangwoo123")
                    .email("sktkddn777@gmail.com")
                    .password("myPassword")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_PASSWORD.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_PASSWORD.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }

        @Test
        @DisplayName("닉네임이 제약조건에 맞지 않는 오류")
        public void invalidNicknameTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("no")
                    .email("sktkddn777@gmail.com")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_NICKNAME.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_NICKNAME.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }

        @Test
        @DisplayName("이메일이 제약조건에 맞지 않는 오류")
        public void invalidEmailTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("sangwoo123")
                    .email("sktkddn777")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(INVALID_MEMBER_EMAIL.getCode())))
                    .andExpect(jsonPath("$.message", is(INVALID_MEMBER_EMAIL.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }

        @Test
        @DisplayName("아이디가 중복되는 오류")
        public void duplicatedIdTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize)))
                    .nickname("sangwoo123")
                    .email("sktkddn777@gmail.com")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(EXIST_MEMBER_ID.getCode())))
                    .andExpect(jsonPath("$.message", is(EXIST_MEMBER_ID.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isNotEmpty()
            );
        }

        @Test
        @DisplayName("닉네임이 중복되는 오류")
        public void duplicatedNicknameTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("tester".concat(Integer.toString(totalMemberSize)))
                    .email("sktkddn777@gmail.com")
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(EXIST_MEMBER_NICKNAME.getCode())))
                    .andExpect(jsonPath("$.message", is(EXIST_MEMBER_NICKNAME.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }

        @Test
        @DisplayName("이메일이 중복되는 오류")
        public void duplicatedEmailTest() throws Exception {
            // given
            int totalMemberSize = memberRepository.findAll().size();

            SaveDTO.Request request = SaveDTO.Request.builder()
                    .id("testId".concat(Integer.toString(totalMemberSize + 1)))
                    .nickname("sangwoo123")
                    .email("tester".concat(Integer.toString(totalMemberSize)).concat("@gmail.com"))
                    .password("myPassword123@")
                    .sns(false)
                    .build();

            // when & then
            mvc.perform(post(BASE_URL.concat(REGISTER_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("register"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code", is(EXIST_MEMBER_EMAIL.getCode())))
                    .andExpect(jsonPath("$.message", is(EXIST_MEMBER_EMAIL.getMessage())));

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());
            assertAll(
                    () -> assertThat(byId).isEmpty()
            );
        }
    }

}