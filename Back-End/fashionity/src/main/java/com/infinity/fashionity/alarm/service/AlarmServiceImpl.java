package com.infinity.fashionity.alarm.service;

import com.infinity.fashionity.alarm.dto.AlarmDTO;
import com.infinity.fashionity.alarm.dto.AlarmDeleteDTO;
import com.infinity.fashionity.alarm.dto.AlarmSendDTO;
import com.infinity.fashionity.alarm.entity.AlarmEntity;
import com.infinity.fashionity.alarm.entity.AlarmType;
import com.infinity.fashionity.alarm.exception.AlarmException;
import com.infinity.fashionity.alarm.repository.AlarmRepository;
import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.comments.repository.CommentRepository;
import com.infinity.fashionity.global.exception.AccessDeniedException;
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

import static com.infinity.fashionity.alarm.entity.AlarmType.*;


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
        else if (type.equals(POST_LIKE)) {
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
        else if (type.equals(COMMENT_LIKE)
                || type.equals(COMMENT_POST)) {
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
    @Transactional(readOnly = true)
    public List<AlarmDTO> findAll(Long memberSeq) {
        List<AlarmEntity> all = alarmRepository.findAllByOwnerSeqOrderByCreatedAtDesc(memberSeq);

        return all.stream()
                .map(entity -> {
                    AlarmType alarmType = entity.getAlarmType();

                    return AlarmDTO.builder()
                            .imageUrl(alarmType.equals(AlarmType.FOLLOW)
                                    ? entity.getPublisher().getProfileUrl()
                                    : entity.getPost().getPostImages().get(0).getUrl())
                            .title(getTitle(entity))
                            .content(getContent(entity))
                            .publisherNickname(entity.getPublisher().getNickname())
                            .postSeq(entity.getPost() != null ? entity.getPost().getSeq() : null)
                            .type(alarmType)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean readAlarm(Long ownerSeq,Long alarmSeq) {
        AlarmEntity alarmEntity = alarmRepository.findById(alarmSeq)
                .orElseThrow(() -> new AlarmException(ErrorCode.ALARM_NOT_FOUND));

        if(alarmEntity.getOwner().getSeq() != ownerSeq){
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        alarmEntity.readAlarm();
        return alarmEntity.isRead();
    }

    @Override
    @Transactional
    public Boolean deleteAlarm(Long ownerSeq,Long alarmSeq) {
        AlarmEntity alarmEntity = alarmRepository.findById(alarmSeq)
                .orElseThrow(() -> new AlarmException(ErrorCode.ALARM_NOT_FOUND));

        if(alarmEntity.getOwner().getSeq() != ownerSeq){
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        alarmRepository.delete(alarmEntity);
        return true;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public String getTitle(AlarmEntity alarm){
        StringBuffer sb = new StringBuffer();
        switch(alarm.getAlarmType()){
            case FOLLOW:
                sb.append(alarm.getPublisher().getNickname()).append(" 님이 회원님을 팔로우했습니다.");
                break;
            case POST_LIKE:
                sb.append("누군가가 회원님의 게시물을 스크랩했습니다.");
                break;
            case COMMENT_LIKE:
                sb.append("누군가가 회원님의 댓글을 좋아합니다.");
                break;
            case COMMENT_POST:
                sb.append(alarm.getPublisher().getNickname()).append("님이 회원님의 게시물에 댓글을 남겼습니다.");
                break;
            default:
                break;
        }
        return sb.toString();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String getContent(AlarmEntity alarm){
        StringBuffer sb = new StringBuffer();
        switch(alarm.getAlarmType()){
            case FOLLOW:
                break;
            case POST_LIKE:
                sb.append(alarm.getPost() != null ? alarm.getPost().getContent() : "");
            case COMMENT_LIKE:
            case COMMENT_POST:
                sb.append(alarm.getComment() != null ? alarm.getComment().getContent() : "");
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
