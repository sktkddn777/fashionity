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
@SQLDelete(sql = "UPDATE alarms SET deleted_at = now() WHERE alarm_seq = ?")
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

    @Column(name="is_readed")
    private boolean read;

    @Enumerated(value=EnumType.STRING)
    private AlarmType alarmType;

    public void readAlarm(){
        this.read = true;
    }
}
