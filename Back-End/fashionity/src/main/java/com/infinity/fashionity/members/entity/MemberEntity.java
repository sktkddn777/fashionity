package com.infinity.fashionity.members.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinity.fashionity.consultants.entity.ReservationEntity;

import com.infinity.fashionity.follows.entity.FollowEntity;
import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.PersonalColor;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.dto.ProfileDTO;
import com.infinity.fashionity.posts.entity.PostEntity;
import lombok.*;

import org.hibernate.annotations.ColumnDefault;

import org.hibernate.annotations.DynamicUpdate;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE Members SET deleted_at = now() WHERE member_seq = ?")
@Where(clause = "deleted_at is null")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@ToString
public class MemberEntity extends CUDEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_seq")
    private Long seq;

    @Column(name = "member_id", length = 20, unique = true, nullable = false)
    private String id;

    @Column(name = "member_pw", length = 60, unique = false, nullable = false)
    private String password;

    @Column(name = "member_nickname", length = 13, unique = true, nullable = false)
    private String nickname;

    @Column(name = "member_email", length = 255, unique = true, nullable = false)
    private String email;

    @ColumnDefault("false")
    @Column(name = "member_sns", unique = false, nullable = false)
    private Boolean sns;

    @Column(name = "member_sns_type", length = 20, unique = false, nullable = true)
    @Enumerated(value=EnumType.STRING)
    private SNSType snsType;

    @Column(name="member_profile_name",unique = false,nullable=true)
    private String profileName;

    @Column(name = "member_profile_url", unique = false, nullable = true, columnDefinition = "TEXT")
    private String profileUrl;

    @Column(name="member_age", unique = false, nullable = true)
    private Integer age;

    @Column(name = "member_profile_intro", length = 50, unique = false, nullable = true)
    private String profileIntro;

    @Enumerated(value=EnumType.STRING)
    @Column(name = "member_gender", length = 20, unique = false, nullable = true)
    private Gender gender;

    @Column(name = "member_height", unique = false, nullable = true)
    private Float height;

    @Column(name = "member_weight", unique = false, nullable = true)
    private Float weight;

    @Enumerated(value=EnumType.STRING)
    @Column(name = "member_personalcolor", length = 20, unique = false, nullable = true)
    private PersonalColor personalcolor;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member",cascade = CascadeType.ALL)
    private List<MemberRoleEntity> memberRoles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<PostEntity> posts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<FollowEntity> follows = new ArrayList<>();

    @Override
    public String toString() {
        return "member : " + id + " email = " + email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateProfileImage(ImageDTO imageInfo){
        //썸네일을 삭제
        if(imageInfo == null){
            this.profileName = this.profileUrl = null;
        }
        //아니면 저장
        else {
            this.profileName = imageInfo.getFileName();
            this.profileUrl = imageInfo.getFileUrl();
        }
    }
    public void updateProfile(ProfileDTO.Request profile) {
        this.profileIntro = profile.getProfileIntro();
        this.nickname = profile.getNickname();
    }
}
