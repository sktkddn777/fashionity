package com.infinity.fashionity.members.service;

import com.infinity.fashionity.members.dto.MemberFollowDTO;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.members.dto.ProfilePostDTO;
import com.infinity.fashionity.members.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public ProfileDTO.Response getMemberProfile(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfilePostDTO.Response getMemberProfilePost(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfilePostDTO.Response getMemberProfileLikedPost(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfilePostDTO.Response getMemberProfileHiddenPost(Long seq, String nickname) {
        return null;
    }

    @Override
    public ProfileDTO.Response editMemberProfile(Long seq, ProfileDTO.Request profile) {
        return null;
    }

    @Override
    public ProfileDTO.Response editMyPassword(Long seq, ProfileDTO.PwRequest data) {
        return null;
    }

    @Override
    public MemberFollowDTO.FollowingResponse getFollowings(Long seq, String nickname) {
        return null;
    }

    @Override
    public MemberFollowDTO.FollowerResponse getFollowers(Long seq, String nickname) {
        return null;
    }
}
