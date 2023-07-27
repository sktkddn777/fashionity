package com.infinity.fashionity.comments.service;

import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.entity.*;
import com.infinity.fashionity.global.exception.AlreadyExistException;
import com.infinity.fashionity.global.exception.NotFoundException;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentReportRepository reportRepository;
    private final CommentLikeRepository likeRepository;
    @Override
    @Transactional(readOnly = true)
    public CommentListDTO.Response getList(CommentListDTO.Request dto) {
        return null;
    }

    @Override
    @Transactional
    public CommentSaveDTO.Response save(CommentSaveDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        CommentEntity comment = CommentEntity.builder()
                .content(dto.getContent())
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);

        return CommentSaveDTO.Response.builder()
                .success(true)
                .seq(comment.getSeq())
                .build();
    }

    @Override
    public CommentUpdateDTO.Response update(CommentUpdateDTO.Request dto) {
        return null;
    }

    @Override
    @Transactional
    public CommentReportDTO.Response report(CommentReportDTO.Request dto) {

        MemberEntity member = memberRepository.findById(dto.getMemberSeq()).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        CommentEntity comment = commentRepository.findById(dto.getCommentSeq()).orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
        CommentReportKey key = CommentReportKey.builder()
                .member(member.getSeq())
                .comment(comment.getSeq())
                .build();

        reportRepository.findById(key).ifPresent(e->{
            throw new AlreadyExistException(ErrorCode.COMMENT_REPORT_ALREADY_EXIST);
        });

        CommentReportEntity report = CommentReportEntity.builder()
                .comment(comment)
                .member(member)
                .category(dto.getReportCategory())
                .content(dto.getReportContent())
                .build();

        reportRepository.save(report);
        return CommentReportDTO.Response.builder()
                .success(true)
                .build();

    }

    @Override
    @Transactional
    public CommentLikeDTO.Response like(CommentLikeDTO.Request dto) {

        MemberEntity member = memberRepository.findById(dto.getMemberSeq()).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        CommentEntity comment = commentRepository.findById(dto.getCommentSeq()).orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        CommentLikeKey key = CommentLikeKey.builder()
                .comment(comment.getSeq())
                .member(member.getSeq())
                .build();

        Optional<CommentLikeEntity> exist = likeRepository.findById(key);
        boolean like = false;
        if(exist.isPresent()){
            likeRepository.delete(exist.get());
            like = false;
        }
        else{
            likeRepository.save(CommentLikeEntity.builder()
                    .comment(comment)
                    .member(member)
                    .build());
            like = true;
        }
        return CommentLikeDTO.Response.builder()
                .like(like)
                .build();
    }

    @Override
    public CommentDeleteDTO.Response delete(CommentDeleteDTO.Request dto) {
        return null;
    }
}
