package com.infinity.fashionity.alarm.entity;

import com.infinity.fashionity.comments.entity.CommentEntity;
import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.posts.entity.PostEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "alarms")
@SQLDelete(sql = "UPDATE Comments SET deleted_at = now() WHERE comment_seq = ?")
@Where(clause = "deleted_at is null")
@DynamicUpdate
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmEntity extends CUDEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="owner_seq")
    private MemberEntity owner;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_seq")
    private PostEntity post;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="comment_seq")
    private CommentEntity comment;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="publisher_seq")
    private MemberEntity publisher;

    private boolean read;

    @Enumerated(value=EnumType.STRING)
    private AlarmType alarmType;

    public String getTitle(){
        StringBuffer sb = new StringBuffer();
        switch(this.alarmType){
            case FOLLOW:
                sb.append(publisher.getNickname()).append(" 님이 회원님을 팔로우했습니다.");
                break;
            case POST_LIKE:
                sb.append("누군가가 회원님의 게시물을 스크랩했습니다.");
                break;
            case COMMENT_LIKE:
                sb.append("누군가가 회원님의 댓글을 좋아합니다.");
                break;
            case COMMENT_POST:
                sb.append(publisher.getNickname()).append("님이 회원님을 팔로우 했습니다.");
                break;
            default:
                break;
        }
        return sb.toString();
    }

    public String getContent(){
        StringBuffer sb = new StringBuffer();
        switch(this.alarmType){
            case FOLLOW:
                break;
            case POST_LIKE:
                sb.append(this.post.getContent());
            case COMMENT_LIKE:
            case COMMENT_POST:
                sb.append(this.comment.getContent());
                break;
            default:
                break;
        }
        return sb.toString();
    }
}
