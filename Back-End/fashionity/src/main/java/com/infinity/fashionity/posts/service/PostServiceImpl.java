package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.*;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;
import com.infinity.fashionity.image.service.ImageService;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.FollowEntity;
import com.infinity.fashionity.members.entity.FollowKey;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.FollowRepository;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.entity.*;
import com.infinity.fashionity.posts.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostReportRepository postReportRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PostImageRepository postImageRepository;
    private final FollowRepository followRepository;
    private final ImageService imageService;

    // 게시글 전체 조회
    @Override
    public PostListDTO.Response getAllPosts(PostListDTO.Request dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        String s = dto.getS();
        Long memberSeq = dto.getMemberSeq();

        // s 기준으로 paging처리 (s 기본값 popular)
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> result = null;
        if(s.equals("popular")){
            result = postLikeRepository.findPostsOrderByLikesDesc(pageable);
        }
        else{
            result = postLikeRepository.findPostsOrderByCreatedAt(pageable);
        }
        // page, size에 맞게 게시물 목록 가져오기
        List<PostListDTO.Post> posts = result.getContent().stream()
                .map(obj -> {
                    PostEntity entity = (PostEntity)obj[0];
                    int likeCount = (int)obj[1];
                    List<String> imageUrls = entity.getPostImages().stream()
                            .map(imageEntity -> imageEntity.getUrl())
                            .collect(Collectors.toList());
                    // 좋아요 여부
                    PostLikeKey likeKey = PostLikeKey.builder()
                            .post(entity.getSeq())
                            .member(memberSeq)
                            .build();
                    Optional<PostLikeEntity> postLike = postLikeRepository.findById(likeKey);
                    boolean isLike = false;
                    if(postLike.isPresent()){
                        isLike = true;
                    }
                    return PostListDTO.Post.builder()
                            .postSeq(entity.getSeq())
                            .name(entity.getMember().getNickname())
                            .profileImg(entity.getMember().getProfileUrl())
                            .content(entity.getContent())
                            .commentCount(entity.getCommentCount())
                            .likeCount(likeCount)
                            .images(imageUrls)
                            .liked(isLike)
                            .build();
                }).collect(Collectors.toList());
        return PostListDTO.Response.builder()
                .posts(posts)
                .build();
    }

    // 게시글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public PostDetailDTO.Response getPost(PostDetailDTO.Request dto) {
        Long postSeq = dto.getPostSeq();
        Long memberSeq = dto.getMemberSeq();

        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 이미지
        List<String> imageUrls = post.getPostImages().stream()
                .map(postImageEntity -> postImageEntity.getUrl())
                .collect(Collectors.toList());

        // 좋아요 여부
        // 게시글 좋아요 복합키 생성
        PostLikeKey likeKey = PostLikeKey.builder()
                .post(postSeq)
                .member(memberSeq)
                .build();
        // 복합키 있는지 확인
        Optional<PostLikeEntity> postLike = postLikeRepository.findById(likeKey);
        boolean isLike = false;
        if(postLike.isPresent()){
            isLike = true;
        }

        // 팔로잉 여부
        // 팔로우 복합키 생성
        FollowKey followKey = FollowKey.builder()
                .followedMember(post.getMember().getSeq())
                .member(memberSeq)
                .build();
        // 복합키 있는지 확인
        Optional<FollowEntity> follow = followRepository.findById(followKey);
        boolean isFollow = false;
        if(follow.isPresent()){
            isFollow = true;
        }

        // 게시글 만들기
        PostDetailDTO.Post postDetail = PostDetailDTO.Post.builder()
                .name(post.getMember().getNickname())
                .profileImg(post.getMember().getProfileIntro())
                .postSeq(postSeq)
                .content(post.getContent())
                .images(imageUrls)
                .liked(isLike)
                .following(isFollow)
                .commentCount(post.getCommentCount())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();

        return PostDetailDTO.Response.builder()
                .post(postDetail)
                .build();
    }

    // 게시글 등록
    @Override
    @Transactional
    public PostSaveDTO.Response savePost(PostSaveDTO.Request dto) {
        // 입력값 검증
        Long memberSeq = dto.getMemberSeq();
        String content = dto.getContent();
        List<MultipartFile> images = dto.getImages();
        List<String> hashtags = dto.getHashtag();
        if(memberSeq == null || images.size() > 4 || StringUtils.isBlank(content)){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // PostEntity 등록
        PostEntity post = PostEntity.builder()
                .content(content)
                .member(member)
                .build();

        // 영속화
        postRepository.save(post);

        // 해시태그 등록 및 영속화
        for(int i = 0; i < hashtags.size(); i++){
            HashtagEntity hashtag = HashtagEntity.builder()
                .name(hashtags.get(i))
                .build();

            if(!postHashtagRepository.findAll().contains(hashtags.get(i))){
                hashtagRepository.save(hashtag);
            }

            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            postHashtagRepository.save(postHashtag);
        }

        //먼저 이미지를 저장소에 저장
        ImageSaveDTO.Response savedImage = imageService.save(ImageSaveDTO.Request.builder()
                .images(images)
                .build());

        // 이미지 정보를 DB에 저장
        List<ImageDTO> imageDTOList = savedImage.getImageInfos();
        for(int i = 0; i < imageDTOList.size(); i++){
            PostImageEntity image = PostImageEntity.builder()
                    .url(imageDTOList.get(i).getFileUrl())
                    .name(imageDTOList.get(i).getFileName())
                    .post(post)
                    .build();
            postImageRepository.save(image);
        }

        return PostSaveDTO.Response.builder()
                .success(true)
                .postSeq(post.getSeq())
                .build();
    }
    // 게시글 수정 (이미지, 해시태그 수정!!!!!)
    @Override
    @Transactional
    public PostUpdateDTO.Response updatePost(PostUpdateDTO.Request dto) {
        Long postSeq = dto.getPostSeq();
        Long memberSeq = dto.getMemberSeq();
        String content = dto.getContent();
        ArrayList<String> images = dto.getImages();
        ArrayList<String> hashtags = dto.getHashtag();

        // 입력값 검증
        if(memberSeq == null || postSeq == null || StringUtils.isBlank(content)){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // 게시글 작성한 멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 게시글 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 작성자와 일치하는지 확인
        if(!post.getMember().getSeq().equals(memberSeq)){
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        // 이미지
        postImageRepository.deleteByPostSeq(postSeq);
        for(int i = 0; i < images.size(); i++){
            PostImageEntity image = PostImageEntity.builder()
                    .url(images.get(i))
                    .post(post)
                    .build();
            postImageRepository.save(image);
        }

        // 해시태그
        postHashtagRepository.deleteByPostSeq(postSeq);
        for(int i = 0; i < hashtags.size(); i++){
            HashtagEntity hashtag = HashtagEntity.builder()
                    .name(hashtags.get(i))
                    .build();

            if(!postHashtagRepository.findAll().contains(hashtags.get(i))){
                hashtagRepository.save(hashtag);
            }

            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            postHashtagRepository.save(postHashtag);
        }

        // 게시글 업데이트
        post.updateContent(content);

        return PostUpdateDTO.Response.builder()
                .success(true)
                .build();
    }

    // 게시글 삭제
    @Override
    @Transactional
    public PostDeleteDTO.Response deletePost(PostDeleteDTO.Request dto) {
        Long postSeq = dto.getPostSeq();
        Long memberSeq = dto.getMemberSeq();

        // 입력값 검증
        if(postSeq == null || memberSeq == null){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // member 있는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // post 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 작성자, 권한자만 삭제 가능
        if(memberSeq.equals(post.getMember().getSeq())
                || !member.getMemberRoles().contains(MemberRole.ADMIN)){
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        // 영속화
        postRepository.delete(post);

        return PostDeleteDTO.Response.builder()
                .success(true)
                .build();
    }

    // 게시글 좋아요
    @Override
    @Transactional
    public PostLikeDTO.Response likePost(PostLikeDTO.Request dto) {
        Long postSeq = dto.getPostSeq();
        Long memberSeq = dto.getMemberSeq();

        // 입력값 검증
        if(postSeq == null || memberSeq == null){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 게시글 좋아요 복합키 생성
        PostLikeKey key = PostLikeKey.builder()
                .post(postSeq)
                .member(memberSeq)
                .build();

        // 게시글 좋아요 눌렀는지 확인
        Optional<PostLikeEntity> postLike = postLikeRepository.findById(key);
        boolean like = false;
        if(postLike.isPresent()){
            postLikeRepository.delete(postLike.get());
            like = false;
        }else{
            postLikeRepository.save(PostLikeEntity.builder()
                    .post(post)
                    .member(member)
                    .build());
            like = true;
        }
        return PostLikeDTO.Response.builder()
                .like(like)
                .build();
    }

    // 게시글 신고
    @Override
    @Transactional
    public PostReportDTO.Response reportPost(PostReportDTO.Request dto) {
        Long postSeq = dto.getPostSeq();
        Long memberSeq = dto.getMemberSeq();
        String type = dto.getType();
        String content = dto.getContent();

        // 입력값 검증
        if(postSeq == null || memberSeq == null || StringUtils.isBlank(type)){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 게시글 신고 복합키 생성
        PostReportKey key = PostReportKey.builder()
                .member(memberSeq)
                .post(postSeq)
                .build();

        // 게시글 신고 했는지 확인
        postReportRepository.findById(key).ifPresent(e->{
            throw new AlreadyExistException(ErrorCode.POST_REPORT_ALREADY_EXIST);
        });

        // 게시글 신고 등록
        PostReportEntity report = PostReportEntity.builder()
                .post(post)
                .member(member)
                .category(type)
                .content(content)
                .build();

        // 영속화
        postReportRepository.save(report);

        return PostReportDTO.Response.builder()
                .success(true)
                .build();
    }
}
