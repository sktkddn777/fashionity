package com.infinity.fashionity;

import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FashionityApplicationTests {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void contextLoads() {

    }

}
