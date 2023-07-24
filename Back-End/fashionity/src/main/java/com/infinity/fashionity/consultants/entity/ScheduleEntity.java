package com.infinity.fashionity.consultants.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "consultant_schedule")
public class ScheduleEntity {
    @Id
    @Column(name = "schedule_seq")
    private long seq;
}
