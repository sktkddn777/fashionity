package com.infinity.fashionity.comments.service;

import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    @Override
    public CommentListDTO.Response getAll(CommentListDTO.Request dto) {
        return null;
    }

    @Override
    @Transactional
    public CommentSaveDTO.Response save(CommentSaveDTO.Request dto) {
        MemberEntity member = memberRepository.findById(dto.getMemberSeq())
                .orElseThrow(() -> new MemberNotFoundException());

        PostEntity post = postRepository.findById(dto.getPostSeq())
                .orElseThrow(() -> new IllegalArgumentException("해당 포스트를 찾을 수 없습니다."));

        CommentEntity comment = CommentEntity.builder()
                .content(dto.getContent())
                .post(post)
                .member(member)
                .build();

        commentRepository.save(comment);

        return CommentSaveDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    public CommentReportDTO.Response report(CommentReportDTO.Request dto) {
        return null;
    }

    @Override
    public CommentLikeDTO.Response like(CommentLikeDTO.Request dto) {
        return null;
    }

    @Override
    public CommentDeleteDTO.Response delete(CommentDeleteDTO.Request dto) {
        return null;
    }
}
