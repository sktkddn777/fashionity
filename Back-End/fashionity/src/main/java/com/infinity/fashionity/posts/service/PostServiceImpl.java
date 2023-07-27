package com.infinity.fashionity.posts.service;

import com.infinity.fashionity.global.exception.AlreadyExistException;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.NotFoundException;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.dto.*;
import com.infinity.fashionity.posts.entity.*;
import com.infinity.fashionity.posts.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 게시글 전체 조회
    @Override
    public PostListDTO.Response getAllPosts(PostListDTO.Request dto) {

        return null;
    }

    // 게시글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public PostDetailDTO.Response getPost(PostDetailDTO.Request dto) {
        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        return null;
    }

    // 게시글 등록
    @Override
    public PostSaveDTO.Response savePost(PostSaveDTO.Request dto) {
        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        // PostEntity 등록
        PostEntity post = PostEntity.builder()
                .content(dto.getContent())
                .member(member)
                .build();
        // DB에 등록
        postRepository.save(post);
        // 해시태그 등록 및 DB 저장
        for(int i = 0; i < dto.getHashtag().size(); i++){
            HashtagEntity hashtag = HashtagEntity.builder()
                .name(dto.getHashtag().get(i))
                .build();
            PostHashtagEntity postHashtag = PostHashtagEntity.builder()
                    .hashtag(hashtag)
                    .post(post)
                    .build();
            hashtagRepository.save(hashtag);
            postHashtagRepository.save(postHashtag);
        }
        // 이미지 등록 및 DB 저장
        for(int i = 0; i < dto.getImages().size(); i++){
            PostImageEntity image = PostImageEntity.builder()
                    .url(dto.getImages().get(i))
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
    public PostUpdateDTO.Response updatePost(PostUpdateDTO.Request dto) {
        return null;
    }

    // 게시글 삭제
    @Override
    public PostDeleteDTO.Response deletePost(PostDeleteDTO.Request dto) {
        return null;
    }

    // 게시글 좋아요
    @Override
    @Transactional
    public PostLikeDTO.Response likePost(PostLikeDTO.Request dto) {
        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        // 게시글 좋아요 복합키 생성
        PostLikeKey key = PostLikeKey.builder()
                .post(post.getSeq())
                .member(member.getSeq())
                .build();
        // 게시글 좋아요 눌렀는지 확인
        Optional<PostLikeEntity> exist = postLikeRepository.findById(key);
        boolean like = false;
        if(exist.isPresent()){
            postLikeRepository.delete(exist.get());
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
    public PostReportDTO.Response reportPost(PostReportDTO.Request dto) {
        // member가 있는지 확인
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        // 게시글 있는지 확인
        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
        // 게시글 신고 복합키 생성
        PostReportKey key = PostReportKey.builder()
                .member(member.getSeq())
                .post(post.getSeq())
                .build();
        // 게시글 신고 했는지 확인
        postReportRepository.findById(key).ifPresent(e->{
            throw new AlreadyExistException(ErrorCode.POST_REPORT_ALREADY_EXIST);
        });
        // 게시글 신고 등록
        PostReportEntity report = PostReportEntity.builder()
                .post(post)
                .member(member)
                .category(dto.getType())
                .content(dto.getContent())
                .build();
        // DB에 저장
        postReportRepository.save(report);

        return PostReportDTO.Response.builder()
                .success(true)
                .build();
    }
}
