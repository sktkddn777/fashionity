package com.infinity.fashionity.members.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.dto.LoginDTO;
import com.infinity.fashionity.members.dto.SaveDTO;
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
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

import static com.infinity.fashionity.global.exception.ErrorCode.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.Matchers.notNullValue;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    private final String BASE_URL = "/api/v1/members";
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
        @DisplayName("register success")
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
        @DisplayName("invalid id exception")
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
        @DisplayName("invalid password exception")
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
        @DisplayName("invalid nickname exception")
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
        @DisplayName("invalid email exception")
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
        @DisplayName("id duplicate exception")
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
        @DisplayName("nickname duplicate exception")
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
        @DisplayName("email duplicate exception")
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

    @Nested
    @DisplayName("Member Login Test")
    public class MemberLoginTest {

        private final String LOGIN_REQUEST_URL = "/login";
        Long randomMemberId;

        @Test
        @DisplayName("login success")
        public void memberLoginSuccessTest() throws Exception {
            // given
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();
            randomMemberId = Math.abs(random.nextLong())%totalMemberSize;

            LoginDTO.Request request = LoginDTO.Request.builder()
                    .id("testId".concat(Long.toString(randomMemberId)))
                    .password("testPassword123@")
                    .build();



            // when & then
            MvcResult result = mvc.perform(post(BASE_URL.concat(LOGIN_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken", notNullValue()))
                    .andReturn();

            String content = result.getResponse().getContentAsString(UTF_8);
            LoginDTO.Response response = objectMapper.readValue(content, LoginDTO.Response.class);

            Optional<MemberEntity> byId = memberRepository.findById(request.getId());

            int idGetByClaims = (int) jwtProvider.getClaims(response.getAccessToken()).get("id");
            int idGetByEntity = byId.get().getSeq().intValue();
            assertThat(idGetByClaims).isEqualTo(idGetByEntity);
        }

        @Test
        @DisplayName("idNotFoundException")
        public void idNotFoundException() throws Exception{
            // given
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();
            randomMemberId = Math.abs(random.nextLong())%totalMemberSize;

            LoginDTO.Request request = LoginDTO.Request.builder()
                    .id("testId")
                    .password("testPassword123@")
                    .build();

            // when & then
            MvcResult result = mvc.perform(post(BASE_URL.concat(LOGIN_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code", is(MEMBER_NOT_FOUND.getCode())))
                    .andExpect(jsonPath("$.message", is(MEMBER_NOT_FOUND.getMessage())))
                    .andReturn();
        }

        @Test
        @DisplayName("idOrPasswordNotMatchException")
        public void idOrPasswordNotMatchException() throws Exception{
            // given
            Random random = new Random();
            int totalMemberSize = memberRepository.findAll().size();
            randomMemberId = Math.abs(random.nextLong())%totalMemberSize;

            LoginDTO.Request request = LoginDTO.Request.builder()
                    .id("testId".concat(Long.toString(randomMemberId)))
                    .password("notTestPassword123@")
                    .build();

            // when & then
            MvcResult result = mvc.perform(post(BASE_URL.concat(LOGIN_REQUEST_URL))
                            .contentType("application/json")
                            .characterEncoding(UTF_8)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(handler().handlerType(MemberController.class))
                    .andExpect(handler().methodName("login"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.code", is(CREDENTIAL_NOT_MATCHED.getCode())))
                    .andExpect(jsonPath("$.message", is(CREDENTIAL_NOT_MATCHED.getMessage())))
                    .andReturn();
        }
    }

    @Nested
    @DisplayName("oauth login test")
    public class OauthLoginTest {


    }
}