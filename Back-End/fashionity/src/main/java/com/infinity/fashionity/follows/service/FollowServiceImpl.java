package com.infinity.fashionity.follows.service;


import com.infinity.fashionity.alarm.dto.AlarmSendDTO;
import com.infinity.fashionity.alarm.entity.AlarmType;
import com.infinity.fashionity.alarm.service.AlarmService;
import com.infinity.fashionity.follows.dto.CheckDTO;
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
    private final AlarmService alarmService;

    @Override
    public FollowDTO.Response follow(Long seq, String nickname) {
        //팔로우 당한사람
        MemberEntity followedMember = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        //팔로우 한 사람
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

        //알람 보내기
        alarmService.sendAlarm(AlarmSendDTO.Request.builder()
                .ownerSeq(followedMember.getSeq())
                .publisherSeq(member.getSeq())
                .type(AlarmType.FOLLOW)
                .build());
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

    @Override
    public CheckDTO.Response checkFollow(Long seq, String nickname){
        Long checkMemberSeq = memberRepository.findSeqByNickname(nickname);
        return CheckDTO.Response.builder()
                .isFollowing(followRepository.getIsFollowing(seq, checkMemberSeq))
                .build();
    }
}
