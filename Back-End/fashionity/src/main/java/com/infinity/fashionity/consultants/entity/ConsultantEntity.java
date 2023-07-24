package com.infinity.fashionity.consultants.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConsultantEntity {
    @Id
    @Column(name = "consultant_info_seq")
    private long seq;
}
