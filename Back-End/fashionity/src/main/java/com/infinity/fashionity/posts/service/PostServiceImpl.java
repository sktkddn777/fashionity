package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.alarm.dto.AlarmSendDTO;
import com.infinity.fashionity.alarm.entity.AlarmType;
import com.infinity.fashionity.alarm.service.AlarmService;
import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.follows.entity.FollowKey;
import com.infinity.fashionity.follows.repository.FollowRepository;
import com.infinity.fashionity.global.exception.*;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageDeleteDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;
import com.infinity.fashionity.image.service.ImageService;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.entity.*;
import com.infinity.fashionity.posts.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * TODO :
 * 게시글 전체 조회, 게시글 단일 조회, like, report 리팩토링
 * */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostReportRepository postReportRepository;
    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;
    private final PostImageRepository postImageRepository;
    private final FollowRepository followRepository;
    private final ImageService imageService;
    private final AlarmService alarmService;

    // 게시글 전체 조회
    @Override
    public PostListDTO.Response getAllPosts(PostListDTO.Request dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        String s = dto.getS();
        String h = dto.getH();
        Long memberSeq = dto.getMemberSeq();


        // s 기준으로 paging처리 (s 기본값 popular)
        Pageable pageable = PageRequest.of(page, size);
        Page<Object[]> result = null;

        //해시태그를 기준으로 정렬
        if(!StringUtils.isBlank(h)) {
            if (s.equals("popular")) {
                result = postRepository.findAllWithHashtagOrderByLikeCount(h,pageable);
            } else {
                result = postRepository.findAllWithHashtagOrderByCreatedAt(h,pageable);
            }
        }
        else {
            if (s.equals("popular")) {
                result = postRepository.findPostsOrderByLikesDesc(pageable);
            } else {
                result = postRepository.findPostsOrderByCreatedAt(pageable);
            }
        }
        // page, size에 맞게 게시물 목록 가져오기
        List<PostListDTO.Post> posts = result.getContent().stream()
                .map(obj -> {
                    PostEntity entity = (PostEntity) obj[0];
                    int likeCount = ((Long) obj[1]).intValue();
                    List<String> imageUrls = postImageRepository.findAllByPost(entity)
                            .stream()
                            .map(PostImageEntity::getUrl)
                            .collect(Collectors.toList());
                    // 좋아요 여부
                    PostLikeKey likeKey = PostLikeKey.builder()
                            .post(entity.getSeq())
                            .member(memberSeq)
                            .build();

                    Optional<PostLikeEntity> postLike = postLikeRepository.findById(likeKey);
                    boolean isLike = false;
                    if (postLike.isPresent()) {
                        isLike = true;
                    }
                    return PostListDTO.Post.builder()
                            .postSeq(entity.getSeq())
                            .name(entity.getMember().getNickname())
                            .profileImg(entity.getMember().getProfileUrl())
                            .content(entity.getContent())
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

        // 좋아요

        MemberEntity member = null;
        //인증정보가 들어오면 찾음
        if(memberSeq != null){
            member = memberRepository.findById(memberSeq).orElseThrow(()->new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        }
        //아니라면 미인증 사용자기 떄문에 liked 상태를 false로 만들기 위해 존재하지 않는 값으로 만듬
        else{
            member = MemberEntity.builder()
                    .seq(-1l)
                    .build();
        }

        // 게시글 좋아요 복합키 생성
        PostLikeKey likeKey = PostLikeKey.builder()
                .post(postSeq)
                .member(memberSeq)
                .build();
        // 복합키 있는지 확인
        Optional<PostLikeEntity> postLike = postLikeRepository.findById(likeKey);
        boolean isLike = false;
        if (postLike.isPresent()) {
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
        if (follow.isPresent()) {
            isFollow = true;
        }

        // 내가 쓴 글인지 확인
        boolean isMyPost = false;
        if(memberSeq == post.getMember().getSeq()){
            isMyPost = true;
        }

        // 게시글 만들기
        PostDetailDTO.Post postDetail = PostDetailDTO.Post.builder()
                .name(post.getMember().getNickname())
                .profileImg(post.getMember().getProfileUrl())
                .postSeq(postSeq)
                .content(post.getContent())
                .images(imageUrls)
                .hashtags(post.getPostHashtags().stream().map(hashtag->hashtag.getHashtag().getName()).collect(Collectors.toList()))
                .liked(isLike)
                .following(isFollow)
                .isMyPost(isMyPost)
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
        List<String> hashtags = dto.getHashtags();
        if (memberSeq == null || images.size() > 4 || images.isEmpty() || StringUtils.isBlank(content)) {
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

        // 해시태그 등록
        List<PostHashtagEntity> hashtagEntities = new ArrayList<>();
        for (int i = 0; i < hashtags.size(); i++) {
            HashtagEntity hashtag = hashtagRepository.findByName(hashtags.get(i))
                    .orElse(HashtagEntity.builder()
                            .name(hashtags.get(i))
                            .build());

            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            hashtagEntities.add(postHashtag);
        }
        //모두 영속화
        postHashtagRepository.saveAll(hashtagEntities);

        //먼저 이미지를 저장소에 저장
        ImageSaveDTO.Response savedImage = imageService.save(ImageSaveDTO.Request.builder()
                .images(images)
                .build());

        // 이미지 정보를 DB에 저장
        List<ImageDTO> imageDTOList = savedImage.getImageInfos();
        for (int i = 0; i < imageDTOList.size(); i++) {
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
        List<String> hashtags = dto.getHashtag();

        // 입력값 검증
        if (memberSeq == null || postSeq == null || StringUtils.isBlank(content)) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // 게시글 작성한 멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 게시글 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 작성자와 일치하는지 확인
        if (!post.getMember().getSeq().equals(memberSeq)) {
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        //기존 image, hashtags를 삭제

        // 이미지들을 물리 저장소에서 삭제
//        List<ImageDTO> imageInfos = post.getPostImages().stream()
//                .map(image -> ImageDTO.builder()
//                        .fileName(image.getName())
//                        .fileUrl(image.getUrl())
//                        .build())
//                .collect(Collectors.toList());
//        ImageDeleteDTO.Response delete = imageService.delete(ImageDeleteDTO.Request.builder()
//                .images(imageInfos)
//                .build());
//
//        //이미지 정보들을 삭제
//        postImageRepository.deleteByPostSeq(postSeq);
//
//        //물리저장소에서 삭제가 성공했으면 db에서도 지움
//        postImageRepository.deleteByPostSeq(postSeq);

        //해시태그들 삭제
        postHashtagRepository.deleteByPostSeq(postSeq);

        //이미지를 물리저장소에 저장
//        ImageSaveDTO.Response savedImages = imageService.save(ImageSaveDTO.Request.builder()
//                .images(images)
//                .build());

        //저장된 정보를 db에 저장
//        List<PostImageEntity> collect = savedImages.getImageInfos().stream()
//                .map(imageInfo -> PostImageEntity.builder()
//                        .name(imageInfo.getFileName())
//                        .url(imageInfo.getFileUrl())
//                        .post(post)
//                        .build())
//                .collect(Collectors.toList());
//        postImageRepository.saveAll(collect);

        //해시태그 저장
        List<PostHashtagEntity> hashtagEntities = new ArrayList<>();
        for (int i = 0; i < hashtags.size(); i++) {
            HashtagEntity hashtag = hashtagRepository.findByName(hashtags.get(i))
                    .orElse(HashtagEntity.builder()
                            .name(hashtags.get(i))
                            .build());

            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            hashtagEntities.add(postHashtag);
        }
        postHashtagRepository.saveAll(hashtagEntities);

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
        if (postSeq == null || memberSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // member 있는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // post 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 작성자, 권한자만 삭제 가능
        if (!memberSeq.equals(post.getMember().getSeq())
                && !member.getMemberRoles().contains(MemberRole.ADMIN)) {
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        //물리 저장소에 저장된 이미지들 삭제
        List<PostImageEntity> postImages = post.getPostImages();
        List<ImageDTO> imageInfos = postImages.stream()
                .map(postImage -> ImageDTO.builder()
                        .fileName(postImage.getName())
                        .fileUrl(postImage.getUrl())
                        .build())
                .collect(Collectors.toList());
        imageService.delete(ImageDeleteDTO.Request.builder()
                .images(imageInfos)
                .build());

        //db에서 정보들을 삭제
        postImageRepository.deleteByPostSeq(postSeq);

        //db에서 연결된 해시태그를 삭제
        postHashtagRepository.deleteByPostSeq(postSeq);

        //db에서 post관련 정보 삭제
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
        if (postSeq == null || memberSeq == null) {
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
        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
            like = false;
        } else {
            postLikeRepository.save(PostLikeEntity.builder()
                    .post(post)
                    .member(member)
                    .build());
            like = true;
        }


        //owner에게 알람 보내기 or 지우기
        if(like) {
            alarmService.sendAlarm(AlarmSendDTO.Request.builder()
                    .ownerSeq(post.getMember().getSeq())
                    .publisherSeq(member.getSeq())
                    .type(AlarmType.POST_LIKE)
                    .postSeq(post.getSeq())
                    .build());
        }
        else{

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
        String category = dto.getCategory();
        String content = dto.getContent();

        // 입력값 검증
        if (postSeq == null || memberSeq == null || StringUtils.isBlank(category)) {
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
        postReportRepository.findById(key).ifPresent(e -> {
            throw new AlreadyExistException(ErrorCode.POST_REPORT_ALREADY_EXIST);
        });

        // 게시글 신고 등록
        PostReportEntity report = PostReportEntity.builder()
                .post(post)
                .member(member)
                .category(category)
                .content(content)
                .build();

        // 영속화
        postReportRepository.save(report);

        return PostReportDTO.Response.builder()
                .success(true)
                .build();
    }
}
