package com.woowacourse.zzinbros.post.domain;

import java.util.Arrays;

public enum DisplayStrategy {
    ALL(1),
    FRIEND(2),
    ONLY_ME(3);

    private final int tableValue;

    DisplayStrategy(int tableValue) {
        this.tableValue = tableValue;
    }

    public static DisplayStrategy valueOf(int strategy) {
        return Arrays.stream(values())
                .filter(tableValue -> tableValue.match(strategy))
                .findFirst().orElse(ALL);
    }

    private boolean match(int tableValue) {
        return this.tableValue == tableValue;
    }
}
