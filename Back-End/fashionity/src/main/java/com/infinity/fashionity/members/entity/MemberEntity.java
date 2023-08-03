package com.infinity.fashionity.members.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.infinity.fashionity.consultants.entity.ReservationEntity;

import com.infinity.fashionity.global.entity.CUDEntity;
import com.infinity.fashionity.members.data.Gender;
import com.infinity.fashionity.members.data.SNSType;
import com.infinity.fashionity.members.dto.ProfileDTO;
import lombok.*;

import org.hibernate.annotations.ColumnDefault;

import org.hibernate.annotations.DynamicUpdate;

import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Entity
@Table(name = "members")
@SQLDelete(sql = "UPDATE Members SET deleted_at = now() WHERE member_seq = ?")
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

    @Column(name = "member_profile_url", unique = false, nullable = true, columnDefinition = "TEXT")
    private String profileUrl;

    @Column(name = "member_profile_intro", length = 50, unique = false, nullable = true)
    private String profileIntro;

    @Column(name = "member_gender", length = 20, unique = false, nullable = true)
    private Gender gender;

    @Column(name = "member_height", unique = false, nullable = true)
    private Float height;

    @Column(name = "member_weight", unique = false, nullable = true)
    private Float weight;

    @Column(name = "member_personalcolor", length = 20, unique = false, nullable = true)
    private String personalcolor;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member",cascade = CascadeType.ALL)
    private List<MemberRoleEntity> memberRoles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    @Builder.Default
    private List<ReservationEntity> reservations = new ArrayList<>();

    @Override
    public String toString() {
        return "member : " + id + " email = " + email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void updateProfile(ProfileDTO.Request profile) {
        this.profileUrl = profile.getProfileUrl();
        this.profileIntro = profile.getProfileIntro();
        this.nickname = profile.getNickname();
    }
}
