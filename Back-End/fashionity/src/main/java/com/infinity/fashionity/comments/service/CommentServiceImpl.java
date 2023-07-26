package com.infinity.fashionity.comments.service;

import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.entity.CommentLikeKey;
import com.infinity.fashionity.comments.entity.CommentReportEntity;
import com.infinity.fashionity.comments.entity.CommentReportKey;
import com.infinity.fashionity.comments.exception.AlreadyExistException;
import com.infinity.fashionity.comments.exception.NotFoundException;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public CommentListDTO.Response getList(CommentListDTO.Request dto) {
        return null;
    }

    @Override
    @Transactional
    public CommentSaveDTO.Response save(CommentSaveDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new NotFoundException("해당 멤버를 찾을 수 없습니다."));

        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new NotFoundException("해당 포스트를 찾을 수 없습니다."));

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
    public CommentReportDTO.Response report(CommentReportDTO.Request dto) {
        CommentReportKey key = CommentReportKey.builder()
                .member(dto.getMemberSeq())
                .comment(dto.getCommentSeq())
                .build();

        Optional<CommentReportEntity> exist = reportRepository.findById(key);
        if(exist.isPresent()){
            throw new AlreadyExistException("이미 신고하신 댓글입니다.");
        }
        else{
            MemberEntity member = memberRepository.findById(dto.getMemberSeq()).orElseThrow(() -> new NotFoundException("해당 유저를 찾을 수 없습니다."));
            CommentEntity comment = commentRepository.findById(dto.getCommentSeq()).orElseThrow(() -> new NotFoundException("해당 댓글을 찾을 수 없습니다."));

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

    }

    @Override
    public CommentLikeDTO.Response like(CommentLikeDTO.Request dto) {
        CommentLikeKey key = CommentLikeKey.builder()
                .comment(dto.getCommentSeq())
                .member(dto.getMemberSeq())
                .build();

        memberRepository.findById(dto.getMemberSeq()).orElseThrow(()->new NotFoundException("해당 유저를 찾을 수 없습니다."));

        return null;
    }

    @Override
    public CommentDeleteDTO.Response delete(CommentDeleteDTO.Request dto) {
        return null;
    }
}
