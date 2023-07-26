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
    @Transactional
    void contextLoads() {
        List<MemberEntity> memberList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            memberList.add(MemberEntity.builder()
                    .email("test" + i + "@gmail.com")
                    .id("tester" + i)
                    .gender(Gender.MALE)
                            .sns(false)
                    .password("1112")
                    .nickname("tester" + i)
                    .height(155.5f)
                    .weight(50.5f)
                    .build());
        }
        memberRepository.saveAll(memberList);

        memberRepository.delete(memberList.get(0));

        List<MemberEntity> all = memberRepository.findAll();
        all.stream().forEach(m -> {
            System.out.println(m.getSeq());
        });

    }

}
