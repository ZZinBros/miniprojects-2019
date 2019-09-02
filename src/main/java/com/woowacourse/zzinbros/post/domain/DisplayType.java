package com.woowacourse.zzinbros.post.domain;

import java.util.Arrays;

public enum DisplayType {
    ALL(1),
    FRIEND(2),
    ONLY_ME(3);

    private final int tableValue;

    DisplayType(int tableValue) {
        this.tableValue = tableValue;
    }

    public static DisplayType valueOf(int strategy) {
        return Arrays.stream(values())
                .filter(tableValue -> tableValue.match(strategy))
                .findFirst().orElse(ALL);
    }

    private boolean match(int tableValue) {
        return this.tableValue == tableValue;
    }
}
