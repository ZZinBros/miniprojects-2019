package com.woowacourse.zzinbros.learningtest;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TestForLearn {
    @Test
    void UUID_랜덤_중복_테스트() {
        String[] strings1 = new String[10000000];
        for (int i = 0; i < 10000000; i++) {
            strings1[i] = (UUID.randomUUID().toString());
        }

        assertThat(new HashSet<>(Arrays.asList(strings1)).size()).isEqualTo(10000000);
    }

    @Test
    void 루트_디렉토리_경로() {
        System.out.println(new File("").getAbsoluteFile());
    }
}
