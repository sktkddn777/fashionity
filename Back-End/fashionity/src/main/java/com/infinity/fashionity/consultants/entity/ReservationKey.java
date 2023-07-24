package com.infinity.fashionity.consultants.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class ReservationKey implements Serializable {
    private long schedule;
    private long consultant;
}
