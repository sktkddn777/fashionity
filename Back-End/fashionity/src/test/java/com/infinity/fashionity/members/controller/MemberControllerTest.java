package com.infinity.fashionity.members.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.auth.dto.LoginDTO;
import com.infinity.fashionity.auth.dto.SaveDTO;
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
}