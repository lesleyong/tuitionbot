package com.tele.tuitionbot.enums;

import lombok.Getter;

@Getter
public enum MainActions {
    MY_POINTS("My Points"),
    REDEMPTION("Redemption"),
    REFER("Refer A Friend");

    private String name;

    MainActions(String name) {
        this.name = name;
    }
}
