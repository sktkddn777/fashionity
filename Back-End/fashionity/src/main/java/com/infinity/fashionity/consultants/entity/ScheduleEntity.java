package com.infinity.fashionity.consultants.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScheduleEntity {
    @Id
    @Column(name = "schedule_seq")
    private long seq;
}
