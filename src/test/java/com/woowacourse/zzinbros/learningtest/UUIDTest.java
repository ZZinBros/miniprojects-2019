package com.woowacourse.zzinbros.learningtest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UUIDTest {
    @Test
    void name() {
        String[] strings1 = new String[10000000];
        for (int i = 0; i < 10000000; i++) {
            strings1[i] = (UUID.randomUUID().toString());
        }

        assertThat(new HashSet<>(Arrays.asList(strings1)).size()).isEqualTo(10000000);
    }
}
