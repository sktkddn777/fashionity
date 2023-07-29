package com.infinity.fashionity.comments.service;

import com.infinity.fashionity.comments.dto.*;
import com.infinity.fashionity.comments.entity.*;
import com.infinity.fashionity.global.exception.*;
import com.infinity.fashionity.comments.repository.CommentLikeRepository;
import com.infinity.fashionity.comments.repository.CommentReportRepository;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentReportRepository reportRepository;
    private final CommentLikeRepository likeRepository;

    @Override
    @Transactional(readOnly = true)
    public CommentListDTO.Response getList(CommentListDTO.Request dto) {
        int page = dto.getPage();
        int size = dto.getSize();
        Long memberSeq = dto.getMemberSeq();
        Long postSeq = dto.getPostSeq();

        if (postSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //post가 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //댓글을 최신순으로 paging처리하기 위함
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        //size와 page에 맞게, 최신순으로 댓글을 가져옴
        Page<CommentEntity> result = commentRepository.findAllByPost(post, pageable);

        List<Comment> comments = result.getContent().stream()
                .map(entity -> {
                    boolean isLike = entity.getLikes().stream()
                            .filter(e -> e.getMember().getSeq() == memberSeq)
                            .findAny()
                            .isPresent();
                    return Comment.builder()
                            .nickname(entity.getMember().getNickname())
                            .content(entity.getContent())
                            .memberSeq(entity.getMember().getSeq())
                            .profileImg(entity.getMember().getProfileUrl())
                            .createdAt(entity.getCreatedAt())
                            .updatedAt(entity.getUpdatedAt())
                            .likeCnt(entity.getLikes().size())
                            .liked(isLike)
                            .build();
                })
                .collect(Collectors.toList());

        return CommentListDTO.Response.builder()
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .comments(comments)
                .page(result.getNumber())
                .build();
    }

    @Override
    @Transactional
    public CommentSaveDTO.Response save(CommentSaveDTO.Request dto) {
        Long memberSeq = dto.getMemberSeq();
        Long postSeq = dto.getPostSeq();
        String content = dto.getContent();

        //입력값 검증
        if (memberSeq == null || StringUtils.isBlank(content) || postSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //post가 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //위의 member, post를 이용하여 comment entity 만들기
        CommentEntity comment = CommentEntity.builder()
                .content(content)
                .post(post)
                .member(member)
                .build();

        //영속화
        commentRepository.save(comment);

        //Response 리턴
        return CommentSaveDTO.Response.builder()
                .success(true)
                .seq(comment.getSeq())
                .build();
    }

    @Override
    @Transactional
    public CommentUpdateDTO.Response update(CommentUpdateDTO.Request dto) {
        Long memberSeq = dto.getMemberSeq();
        Long commentSeq = dto.getCommentSeq();
        Long postSeq = dto.getPostSeq();
        String content = dto.getContent();

        //입력값 검증
        if (memberSeq == null || StringUtils.isBlank(content) || postSeq == null || commentSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //댓글을 작성한 멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //post가 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //comment가 존재하는지 확인
        CommentEntity comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        //해당 댓글이 자신의 댓글이 아니면 ERROR THROW
        if (comment.getMember().getSeq() != member.getSeq()) {
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        //comment를 update해줌
        comment.updateContent(content);
        return CommentUpdateDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public CommentReportDTO.Response report(CommentReportDTO.Request dto) {
        Long memberSeq = dto.getMemberSeq();
        Long commentSeq = dto.getCommentSeq();
        Long postSeq = dto.getPostSeq();
        String reportCategory = dto.getReportCategory();
        String reportContent = dto.getReportContent();

        //입력값 검증
        if (memberSeq == null || commentSeq == null || postSeq == null
                || StringUtils.isBlank(reportCategory)) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //post가 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //comment가 존재하는지 확인
        CommentEntity comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        //복합키 생성
        CommentReportKey key = CommentReportKey.builder()
                .member(memberSeq)
                .comment(commentSeq)
                .build();

        //만약 이미 신고했다면 throw
        reportRepository.findById(key).ifPresent(e -> {
            throw new AlreadyExistException(ErrorCode.COMMENT_REPORT_ALREADY_EXIST);
        });

        //report entity 생성
        CommentReportEntity report = CommentReportEntity.builder()
                .comment(comment)
                .member(member)
                .category(reportCategory)
                .content(reportContent)
                .build();

        //저장
        reportRepository.save(report);

        return CommentReportDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public CommentLikeDTO.Response like(CommentLikeDTO.Request dto) {
        Long memberSeq = dto.getMemberSeq();
        Long commentSeq = dto.getCommentSeq();
        Long postSeq = dto.getPostSeq();

        //입력값 검증
        if (memberSeq == null || commentSeq == null || postSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //post가 존재하는지 확인
        postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //comment가 존재하는지 확인
        CommentEntity comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        //복합키 생성
        CommentLikeKey key = CommentLikeKey.builder()
                .comment(comment.getSeq())
                .member(member.getSeq())
                .build();

        //해당 복합키로 엔티티를 찾아옴
        Optional<CommentLikeEntity> commentLike = likeRepository.findById(key);

        boolean like = false;
        if (commentLike.isPresent()) {
            likeRepository.delete(commentLike.get());
            like = false;
        } else {
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
        Long commentSeq = dto.getCommentSeq();
        Long memberSeq = dto.getMemberSeq();
        Long postSeq = dto.getPostSeq();

        //validation
        if (memberSeq == null || postSeq == null || commentSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //member가 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //post가 존재하는지 확인
        PostEntity post = postRepository.findById(postSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));

        //comment가 존재하는지 확인
        CommentEntity comment = commentRepository.findById(commentSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));

        //자신의 댓글이 아니거나 권한이 존재하지 않으면 삭제 불가
        if (member.getSeq() != comment.getMember().getSeq()
                && !member.getMemberRoles().contains(MemberRole.ADMIN)) {
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        //삭제
        commentRepository.delete(comment);
        return CommentDeleteDTO.Response.builder()
                .success(true)
                .build();
    }
}
