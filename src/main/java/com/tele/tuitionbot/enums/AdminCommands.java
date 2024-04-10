package com.tele.tuitionbot.enums;

import lombok.Getter;

@Getter
public enum AdminCommands {
    VIEW_SCOREBOARD("/scoreboard");

    private String command;

    AdminCommands(String command) {
        this.command = command;
    }
}
