package com.infinity.fashionity.members.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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

    private final String BASE_URL = "/api/v1";
    private List<MemberEntity> memberList = new ArrayList<>();


    @BeforeEach
    public void dummyInsert() {
        IntStream.rangeClosed(1, 10).forEach(i -> {
            MemberEntity member = MemberEntity.builder()
                    .id("tester".concat(Integer.toString(i)))
                    .password(passwordEncoder.encode("testPassword"))
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
         * 아이디: 6 ~ 20 ,
         * 비밀번호: 5 ~ 60,
         *
         */

        private final String REGISTER_REQUEST_URL = "/register";

        @Test
        @DisplayName("정상 회원가입")
        public void memberSaveSuccessTest() {
            Random random = new Random();
        }
    }

}