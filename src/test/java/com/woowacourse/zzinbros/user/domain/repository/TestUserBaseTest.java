package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.TempUser;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;

import java.util.HashMap;
import java.util.Map;

public class TestUserBaseTest {
    protected final static Map<Integer, TempUser> SAMPLE_USERS;
    protected final static int SAMPLE_ONE = 1;
    protected final static int SAMPLE_TWO = 2;
    protected final static int SAMPLE_THREE = 3;
    protected final static int SAMPLE_FOUR = 4;

    static {
        SAMPLE_USERS = new HashMap<>();
        SAMPLE_USERS.put(SAMPLE_ONE, new TempUser(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_TWO, new TempUser(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_THREE, new TempUser(UserTest.BASE_NAME, "3@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_FOUR, new TempUser(UserTest.BASE_NAME, "4@mail.com", UserTest.BASE_PASSWORD));
    }
}
