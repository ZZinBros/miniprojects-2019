package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.AlreadyFriendRequestExist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FriendServiceTest extends UserBaseTest {

    @MockBean
    private FriendRepository friendRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private FriendService friendService;

    User me;
    User other;

    @BeforeEach
    void setUp() {
        me = SAMPLE_USERS.get(SAMPLE_ONE);
        other = SAMPLE_USERS.get(SAMPLE_TWO);
    }

    @Test
    void requestSuccessTest() {
        Friend friend = Friend.of(me, other);
        given(friendRepository.save(friend)).willReturn(friend);
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(false);

        Friend actual = friendService.sendFriendRequest(me, other);

        assertEquals(friend, actual);
    }

    @Test
    void sendFriendSuccessTest1() {
        User from = userSampleOf(SAMPLE_ONE);
        User to = userSampleOf(SAMPLE_TWO);
        Friend expected = mockingId(Friend.of(from, to), 1L);

        given(userService.findUserById(LOGIN_USER_DTO.getId())).willReturn(from);
        given(userService.findUserById(SAMPLE_TWO)).willReturn(to);
        given(friendRepository.existsByFromAndTo(from, to)).willReturn(false);
        given(friendRepository.save(expected)).willReturn(expected);

        Friend actual = friendService.sendFriendRequest1(LOGIN_USER_DTO, FRIEND_REQUEST_DTO);
        assertEquals(expected, actual);
    }


    @Test
    void requestFailWhenUserExistsTest() {
        Friend friend = Friend.of(me, other);
        given(friendRepository.save(friend)).willReturn(friend);
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(true);

        assertThatThrownBy(() -> friendService.sendFriendRequest(me, other))
                .isInstanceOf(AlreadyFriendRequestExist.class);
    }

    @Test
    void requestFailWhenUserExistsTest1() {
        User from = userSampleOf(SAMPLE_ONE);
        User to = userSampleOf(SAMPLE_TWO);

        given(userService.findUserById(LOGIN_USER_DTO.getId())).willReturn(from);
        given(userService.findUserById(SAMPLE_TWO)).willReturn(to);
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(true);

        assertThatThrownBy(() -> friendService.sendFriendRequest(me, other))
                .isInstanceOf(AlreadyFriendRequestExist.class);
    }

    @Test
    @DisplayName("User에 따른 친구 리스트 반환")
    void findFriendsByUser() {
        Set<Friend> friends = new HashSet<>(Arrays.asList(
                Friend.of(me, other)
        ));
        given(friendRepository.findByFrom(me)).willReturn(friends);

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(other.getId(), other.getName(), other.getEmail())
        ));

        Set<UserResponseDto> actual = friendService.findFriendByUser(me);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User에 따른 친구 리스트 반환")
    void findFriendsByUser1() {
        User me = userSampleOf(SAMPLE_ONE);
        User first = userSampleOf(SAMPLE_TWO);
        User second = userSampleOf(SAMPLE_THREE);

        Friend friend1 = mockingId(Friend.of(me, first), 1L);
        Friend friend2 = mockingId(Friend.of(me, second), 2L);

        Set<Friend> friends = new HashSet<>(Arrays.asList(
                friend1,
                friend2
        ));

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(first.getId(), first.getName(), first.getEmail()),
                new UserResponseDto(second.getId(), second.getName(), second.getEmail())
        ));

        given(userService.findUserById(SAMPLE_ONE)).willReturn(me);
        given(friendRepository.findByFrom(me)).willReturn(friends);

        Set<UserResponseDto> actual = friendService.findFriendByUser1(SAMPLE_ONE);
        assertEquals(expected, actual);
    }
}