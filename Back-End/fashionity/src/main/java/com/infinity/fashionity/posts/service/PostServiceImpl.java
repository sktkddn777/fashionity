package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.*;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.entity.*;
import com.infinity.fashionity.posts.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final CommentRepository commentRepository;

    // 게시글 전체 조회
    @Override
    public PostListDTO.Response getAllPosts(PostListDTO.Request dto) {

        return null;
    }

    // 게시글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public PostDetailDTO.Response getPost(PostDetailDTO.Request dto) {
        long postSeq = dto.getPostSeq();

        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        // 이미지
        List<PostImageEntity> images = post.getPostImages();
        List<String> imageUrls = new ArrayList<>();
        for(int i = 0; i < images.size(); i++){
            String url = images.get(i).getUrl();
            imageUrls.add(url);
        }

        // 좋아요 수
        long likeCount = postLikeRepository.countBySeq(postSeq);

        // 댓글 수
        long commentCount = commentRepository.countBySeq(postSeq);

        // 좋아요 여부


        // 팔로잉 여부

        // 게시글 만들기
        PostDetailDTO.Post PostDetail = PostDetailDTO.Post.builder()
                .name(post.getMember().getNickname())
                .profileImg(post.getMember().getProfileIntro())
                .postSeq(postSeq)
                .content(post.getContent())
                .images(imageUrls)
                .commentCount(commentCount)
                .likeCount(likeCount)
                .build();

        return null;
    }

    // 게시글 등록
    @Override
    @Transactional
    public PostSaveDTO.Response savePost(PostSaveDTO.Request dto) {
        // 입력값 검증
        Long memberSeq = dto.getMemberSeq();
        String content = dto.getContent();
        List<String> images = dto.getImages();
        List<String> hashtags = dto.getHashtag();
        if(memberSeq == null || images.isEmpty() || StringUtils.isBlank(content)){
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
            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            hashtagRepository.save(hashtag);
            postHashtagRepository.save(postHashtag);
        }

        // 이미지 등록 및 DB 저장
        for(int i = 0; i < images.size(); i++){
            PostImageEntity image = PostImageEntity.builder()
                    .url(images.get(i))
                    .post(post)
                    .build();
            postImageRepository.save(image);
        }

        return PostSaveDTO.Response.builder()
                .success(true)
                .build();
    }

    // 게시글 수정
    @Override
    @Transactional
    public PostUpdateDTO.Response updatePost(PostUpdateDTO.Request dto) {
        return null;
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
        if(memberSeq != post.getMember().getSeq()
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
