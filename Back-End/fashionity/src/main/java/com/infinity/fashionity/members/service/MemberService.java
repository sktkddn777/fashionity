package com.infinity.fashionity.members.service;

import com.infinity.fashionity.members.dto.FollowDTO;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.members.dto.ProfilePostDTO;

public interface MemberService {
    ProfileDTO.Response getMemberProfile(Long seq, String nickname);
    ProfilePostDTO.Response getMemberProfilePost(Long seq, String nickname);
    ProfilePostDTO.Response getMemberProfileLikedPost(Long seq, String nickname);
    ProfilePostDTO.Response getMemberProfileHiddenPost(Long seq, String nickname);
    ProfileDTO.Response editMemberProfile(Long seq, ProfileDTO.Request profile);
    ProfileDTO.Response editMyPassword(Long seq, ProfileDTO.PwRequest data);
    FollowDTO.FollowingResponse getFollowings(Long seq, String nickname);
    FollowDTO.FollowerResponse getFollowers(Long seq, String nickname);


}
