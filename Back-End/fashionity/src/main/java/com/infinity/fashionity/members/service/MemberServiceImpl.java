package com.infinity.fashionity.members.service;

import com.infinity.fashionity.consultants.entity.ImageEntity;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.global.utils.RegexUtil;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageDeleteDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;
import com.infinity.fashionity.image.service.ImageService;
import com.infinity.fashionity.members.dto.*;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.exception.CustomValidationException;
import com.infinity.fashionity.members.exception.IdOrPasswordNotMatchedException;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;

import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.entity.PostImageEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.infinity.fashionity.global.exception.ErrorCode.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageService imageService;

    @Override
    public ProfileDTO.Response getMemberProfile(Long seq, String nickname) {
        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followingList = followRepository.findByMember(memberByNickname);
        List<FollowEntity> followedList = followRepository.findByFollowedMember(memberByNickname);
        Integer postsCnt = postRepository.postsCnt(nickname);

        FollowKey followKey = FollowKey.builder()
                .member(seq)
                .followedMember(memberByNickname.getSeq())
                .build();
        Optional<FollowEntity> byId = followRepository.findById(followKey);
        boolean isFollowed = byId.isPresent();

        return ProfileDTO.Response.builder()
                .nickname(memberByNickname.getNickname())
                .profileUrl(memberByNickname.getProfileUrl())
                .profileIntro(memberByNickname.getProfileIntro())
                .postsCnt(postsCnt)
                .followerCnt(followedList.size())
                .followingCnt(followingList.size())
                .isFollowed(isFollowed)
                .myProfile(memberByNickname.getSeq().equals(seq))
                .build();
    }

    @Override
    public ProfilePostDTO.Response getMemberProfilePost(Long seq, String nickname, ProfilePostDTO.Request dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        String memberNickname = dto.getNickname();

        // 등록된 유저가 아니면 예외 처리
        MemberEntity profileMember = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PostEntity> result = postRepository.findPostsByMember(profileMember, pageable);

        List<ProfilePost> profilePosts = result.stream().map(entity -> {
            Long postSeq = entity.getSeq();
            String postThumbnailImage = getThumbnail(entity);
            return ProfilePost.builder()
                    .postSeq(postSeq)
                    .postThumbnailImage(postThumbnailImage)
                    .build();
        }).collect(Collectors.toList());
        return ProfilePostDTO.Response.builder()
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .page(result.getNumber())
                .nickname(memberNickname)
                .profilePosts(profilePosts)
                .build();
    }

    @Override
    public ProfilePostDTO.Response getMemberProfileLikedPost(Long seq, ProfilePostDTO.Request dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        String nickname = dto.getNickname();

        MemberEntity profileMember = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<PostEntity> result = postRepository.findLikedPostsByMember(profileMember, pageable);

        List<ProfilePost> profilePosts = result.stream().map(entity -> {
            Long postSeq = entity.getSeq();
            String postThumbnailImage = getThumbnail(entity);
            return ProfilePost.builder()
                    .postSeq(postSeq)
                    .postThumbnailImage(postThumbnailImage)
                    .build();
        }).collect(Collectors.toList());

        return ProfilePostDTO.Response.builder()
                .nickname(nickname)
                .profilePosts(profilePosts)
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .page(result.getNumber())
                .build();
    }

    private String getThumbnail(PostEntity postEntity) {
        List<PostImageEntity> postImages = postEntity.getPostImages();
        if (postImages.size() == 0)
            return null;
        return postImages.get(0).getUrl();
    }


    @Override
    @Transactional
    public ProfileDTO.Response editMemberProfile(Long seq, ProfileDTO.Request profile) {
        MemberEntity member = memberRepository.findById(seq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followingList = followRepository.findByMember(member);
        List<FollowEntity> followedList = followRepository.findByFollowedMember(member);

        //닉네임 유효성 검사
        boolean blank = StringUtils.isBlank(profile.getNickname());
        if (StringUtils.isBlank(profile.getNickname()) || !RegexUtil.checkNicknameRegex(profile.getNickname()))
            throw new CustomValidationException(INVALID_MEMBER_NICKNAME);

        //닉네임 중복 검사, 다른사람이면서 닉네임이 같으면 throw
        if (member.getSeq() != seq && memberRepository.findByNickname(profile.getNickname()).isPresent())
            throw new CustomValidationException(EXIST_MEMBER_NICKNAME);

        //이미지를 업데이트 할 때 null이 아니면 이미 저장된 이미지 삭제 및 새로 들어온 이미지 저장
        if (profile.getProfileImage() != null) {
            if(member.getProfileName()!=null) {
                //원래 존재하는 프로필정보
                ImageDTO origin = ImageDTO.builder()
                        .fileName(member.getProfileName())
                        .fileUrl(member.getProfileUrl())
                        .build();
                ImageDeleteDTO.Response delete = imageService.delete(ImageDeleteDTO.Request.builder()
                        .images(Arrays.asList(origin))
                        .build());
            }
            //새로운 이미지 저장
            ImageSaveDTO.Response save = imageService.save(ImageSaveDTO.Request.builder()
                    .images(Arrays.asList(profile.getProfileImage()))
                    .build()
            );
            //저장
            List<ImageDTO> imageInfos = save.getImageInfos();
            //저장된 썸네일에 대한 정보를 업데이트
            ImageDTO savedImageInfo = imageInfos.get(0);
            member.updateProfileImage(savedImageInfo);
        }
        else{
            member.updateProfileImage(null);
        }

        member.updateProfile(profile);

        return ProfileDTO.Response.builder()
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .profileIntro(member.getProfileIntro())
                .followerCnt(followedList.size())
                .followingCnt(followingList.size())
                .myProfile(member.getSeq().equals(seq))
                .build();
    }

    @Override
    @Transactional
    public ProfileDTO.PwResponse editMyPassword(Long seq, ProfileDTO.PwRequest data) {
        MemberEntity member = memberRepository.findById(seq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(data.getPassword(), member.getPassword()))
            throw new IdOrPasswordNotMatchedException(CREDENTIAL_NOT_MATCHED);

        if (!RegexUtil.checkPasswordRegex(data.getNewPassword()))
            throw new CustomValidationException(INVALID_MEMBER_PASSWORD);

        member.setPassword(passwordEncoder.encode(data.getNewPassword()));
        return ProfileDTO.PwResponse.builder()
                .success(true)
                .build();
    }

    @Override
    public MemberFollowDTO.FollowingResponse getFollowings(Long seq, String nickname) {

        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followEntityList = followRepository.findByMember(memberByNickname);
        List<Following> followingList = new ArrayList<>();

        followEntityList.stream().forEach(e -> {
            MemberEntity followedMember = e.getFollowedMember();
            FollowKey followKey = FollowKey.builder()
                    .member(seq)
                    .followedMember(followedMember.getSeq())
                    .build();

            followingList.add(Following.builder()
                    .profileUrl(followedMember.getProfileUrl())
                    .nickname(followedMember.getNickname())
                    .isFollowing(followRepository.findById(followKey).isPresent())
                    .build());
        });
        return MemberFollowDTO.FollowingResponse.builder()
                .followings(followingList)
                .build();
    }

    @Override
    public MemberFollowDTO.FollowerResponse getFollowers(Long seq, String nickname) {

        MemberEntity memberByNickname = memberRepository.findByNickname(nickname).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<FollowEntity> followEntityList = followRepository.findByFollowedMember(memberByNickname);
        List<Follower> followerList = new ArrayList<>();

        followEntityList.stream().forEach(e -> {
            MemberEntity followingMember = e.getMember();
            FollowKey followKey = FollowKey.builder()
                    .member(followingMember.getSeq())
                    .followedMember(seq)
                    .build();

            followerList.add(Follower.builder()
                    .profileUrl(followingMember.getProfileUrl())
                    .nickname(followingMember.getNickname())
                    .isFollowing(followRepository.findById(followKey).isPresent())
                    .build());
        });
        return MemberFollowDTO.FollowerResponse.builder()
                .followers(followerList)
                .build();
    }
}
