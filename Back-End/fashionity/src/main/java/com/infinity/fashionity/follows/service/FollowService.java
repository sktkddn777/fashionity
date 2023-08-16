package com.infinity.fashionity.follows.service;

import com.infinity.fashionity.follows.dto.CheckDTO;
import com.infinity.fashionity.follows.dto.FollowDTO;

public interface FollowService {

    FollowDTO.Response follow(Long seq, String nickname);
    FollowDTO.Response unFollow(Long seq, String nickname);

    CheckDTO.Response checkFollow (Long seq, String nickname);
}
