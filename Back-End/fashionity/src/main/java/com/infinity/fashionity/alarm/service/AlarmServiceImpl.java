package com.infinity.fashionity.alarm.service;

import com.infinity.fashionity.alarm.dto.AlarmDTO;
import com.infinity.fashionity.alarm.dto.AlarmSendDTO;
import com.infinity.fashionity.alarm.entity.AlarmEntity;
import com.infinity.fashionity.alarm.entity.AlarmType;
import com.infinity.fashionity.alarm.repository.AlarmRepository;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.NotFoundException;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.posts.entity.PostEntity;
import com.infinity.fashionity.posts.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void sendAlarm(AlarmSendDTO.Request dto) {
        AlarmType type = dto.getType();
        Long ownerSeq = dto.getOwnerSeq();
        Long commentSeq = dto.getCommentSeq();
        Long postSeq = dto.getPostSeq();
        Long publisherSeq = dto.getPublisherSeq();
        AlarmEntity alarm = null;

        MemberEntity owner = memberRepository.findById(ownerSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        MemberEntity publisher = memberRepository.findById(publisherSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        //팔로우 알람
        if (type.equals(AlarmType.FOLLOW)) {
            alarm = AlarmEntity.builder()
                    .alarmType(type)
                    .publisher(publisher)
                    .owner(owner)
                    .read(false)
                    .build();
        }
        //게시글 좋아요 알람
        else if (type.equals(AlarmType.POST_LIKE)) {
            PostEntity post = postRepository.findById(postSeq)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
            alarm = AlarmEntity.builder()
                    .alarmType(type)
                    .publisher(publisher)
                    .owner(owner)
                    .read(false)
                    .post(post)
                    .build();
        }
        //댓글 좋아요 알람 && 댓글 등록
        else if (type.equals(AlarmType.COMMENT_LIKE)
                || type.equals(AlarmType.COMMENT_POST)) {
            PostEntity post = postRepository.findById(postSeq)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
            CommentEntity comment = commentRepository.findById(commentSeq)
                    .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
            alarm = AlarmEntity.builder()
                    .alarmType(type)
                    .publisher(publisher)
                    .owner(owner)
                    .read(false)
                    .post(post)
                    .comment(comment)
                    .build();
        }

        alarmRepository.save(alarm);
    }

    @Override
    public List<AlarmDTO> findAll(Long memberSeq) {
        List<AlarmEntity> all = alarmRepository.findAllByOwnerOrderByCreatedAtDesc(MemberEntity.builder()
                .seq(memberSeq)
                .build());

        return all.stream()
                .map(entity -> {
                    AlarmType alarmType = entity.getAlarmType();

                    return AlarmDTO.builder()
                            .imageUrl(alarmType.equals(AlarmType.FOLLOW)
                                    ? entity.getPublisher().getProfileUrl()
                                    : entity.getPost().getPostImages().get(0).getUrl())
                            .title(entity.getTitle())
                            .content(entity.getContent())
                            .publisherNickname(entity.getPublisher().getNickname())
                            .postSeq(entity.getPost().getSeq())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
