package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;

import java.util.HashMap;
import java.util.Map;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;

public class UserBaseTest {

    protected final static Map<Integer, User> SAMPLE_USERS;
    protected final static int SAMPLE_ONE = 1;
    protected final static int SAMPLE_TWO = 2;
    protected final static int SAMPLE_THREE = 3;
    protected final static int SAMPLE_FOUR = 4;
    protected final static UserResponseDto LOGIN_USER_DTO;
    protected final static FriendRequestDto FRIEND_REQUEST_DTO;

    static {
        SAMPLE_USERS = new HashMap<>();
        SAMPLE_USERS.put(
                SAMPLE_ONE,
                mockingId(new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD), 1L));
        SAMPLE_USERS.put(
                SAMPLE_TWO,
                mockingId(new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD), 2L));
        SAMPLE_USERS.put(
                SAMPLE_THREE,
                mockingId(new User(UserTest.BASE_NAME, "3@mail.com", UserTest.BASE_PASSWORD), 3L));
        SAMPLE_USERS.put(
                SAMPLE_FOUR,
                mockingId(new User(UserTest.BASE_NAME, "4@mail.com", UserTest.BASE_PASSWORD), 4L));

        LOGIN_USER_DTO = new UserResponseDto(1L, UserTest.BASE_NAME, UserTest.BASE_PASSWORD);
        FRIEND_REQUEST_DTO = new FriendRequestDto(2L);
    }

    protected static User userSampleOf(final int sampleNumber) {
        return SAMPLE_USERS.get(sampleNumber);
    }
}
