package com.infinity.fashionity.follows.service;


import com.infinity.fashionity.follows.dto.FollowDTO;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.follows.exception.AlreadyFollowException;
import com.infinity.fashionity.follows.exception.NotFollowException;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.infinity.fashionity.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;

    @Override
    public FollowDTO.Response follow(Long seq, String nickname) {
        MemberEntity followedMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        MemberEntity member = memberRepository.findById(seq)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        // 복합키 생성
        FollowKey followKey = FollowKey.builder()
                .member(seq)
                .followedMember(followedMember.getSeq())
                .build();

        Optional<FollowEntity> byFollowKey = followRepository.findById(followKey);
        if (byFollowKey.isPresent())
            throw new AlreadyFollowException(ALREADY_FOLLOW);

        FollowEntity followEntity = FollowEntity.builder()
                .member(member)
                .followedMember(followedMember)
                .build();

        followRepository.save(followEntity);
        return FollowDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    public FollowDTO.Response unFollow(Long seq, String nickname) {
        MemberEntity followedMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        // 복합키 생성
        FollowKey followKey = FollowKey.builder()
                .member(seq)
                .followedMember(followedMember.getSeq())
                .build();

        FollowEntity followEntity = followRepository.findById(followKey).orElseThrow(() ->
                new NotFollowException(NOT_FOLLOW)
        );

        followRepository.delete(followEntity);

        return FollowDTO.Response.builder()
                .success(true)
                .build();
    }
}
