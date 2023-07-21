package com.infinity.fashionity.global.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 상속용 클래스임을 명시
@EntityListeners(value = AuditingEntityListener.class) // 엔티티의 변화를 감지하고 데이블의 데이터를 조작
abstract public class CEntity {

    @CreatedDate // 생성된 시간 정보를 자동으로 넣어줌
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
