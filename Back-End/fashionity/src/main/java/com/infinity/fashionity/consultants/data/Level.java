package com.infinity.fashionity.consultants.data;

import lombok.Getter;

@Getter
public enum Level {

    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD"),
    PLATINUM("PLATINUM"),
    DIAMOND("DIAMOND"),
    RUBY("RUBY");

    private String level;
    Level(String level) {this.level = level;}
}
