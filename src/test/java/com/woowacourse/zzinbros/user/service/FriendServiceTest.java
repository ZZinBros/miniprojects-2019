package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.AlreadyFriendRequestExist;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    @Test
    @DisplayName("친구 요청을 성공적으로 수행한다")
    void sendFriendSuccessTest() {
        User from = userSampleOf(SAMPLE_ONE);
        User to = userSampleOf(SAMPLE_TWO);
        Friend friend1 = mockingId(Friend.of(from, to), 1L);
        Friend friend2 = mockingId(Friend.of(to, from), 2L);

        given(userService.findUserById(LOGIN_USER_DTO.getId())).willReturn(from);
        given(userService.findUserById(SAMPLE_TWO)).willReturn(to);
        given(friendRepository.existsBySenderAndReceiver(from, to)).willReturn(false);
        given(friendRepository.save(friend1)).willReturn(friend1);
        given(friendRepository.save(friend2)).willReturn(friend2);

        boolean actual = friendService.sendFriendRequest(LOGIN_USER_DTO, FRIEND_REQUEST_DTO);
        assertTrue(actual);
    }

    @Test
    @DisplayName("이미 존재하는 친구 요청이 있을 때 추가할 때 예외가 발생한다")
    void requestFailWhenUserExists() {
        User from = userSampleOf(SAMPLE_ONE);
        User to = userSampleOf(SAMPLE_TWO);

        given(userService.findUserById(LOGIN_USER_DTO.getId())).willReturn(from);
        given(userService.findUserById(SAMPLE_TWO)).willReturn(to);
        given(friendRepository.existsBySenderAndReceiver(from, to)).willReturn(true);

        assertThatThrownBy(() -> friendService.sendFriendRequest(LOGIN_USER_DTO, FRIEND_REQUEST_DTO))
                .isInstanceOf(AlreadyFriendRequestExist.class);
    }

    @Test
    @DisplayName("User에 따른 친구 리스트 반환한다")
    void findFriendsByUser() {
        User me = userSampleOf(SAMPLE_ONE);
        User first = userSampleOf(SAMPLE_TWO);
        User second = userSampleOf(SAMPLE_THREE);

        mockingId(Friend.of(me, first), 1L);
        mockingId(Friend.of(me, second), 2L);
        mockingId(Friend.of(first, me), 3L);

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(first.getId(), first.getName(), first.getEmail())
        ));

        given(userService.findUserById(SAMPLE_ONE)).willReturn(me);

        Set<UserResponseDto> actual = friendService.findFriendsByUser(SAMPLE_ONE);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("유저가 받은 친구 요청을 반환한다")
    void findFriendRequestsByUser() {
        User me = userSampleOf(SAMPLE_ONE);
        User first = userSampleOf(SAMPLE_TWO);
        User second = userSampleOf(SAMPLE_THREE);

        mockingId(Friend.of(second, me), 2L);
        mockingId(Friend.of(first, me), 3L);

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(second.getId(), second.getName(), second.getEmail()),
                new UserResponseDto(first.getId(), first.getName(), first.getEmail())
        ));

        given(userService.findLoggedInUser(LOGIN_USER_DTO)).willReturn(me);

        Set<UserResponseDto> actual = friendService.findFriendRequestsByUser(LOGIN_USER_DTO);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("유저가 받은 친구 요청을 반환한다")
    void findFriendRequestsByID() {
        User me = userSampleOf(SAMPLE_ONE);
        User first = userSampleOf(SAMPLE_TWO);
        User second = userSampleOf(SAMPLE_THREE);

        mockingId(Friend.of(second, me), 2L);
        mockingId(Friend.of(first, me), 3L);

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(second.getId(), second.getName(), second.getEmail()),
                new UserResponseDto(first.getId(), first.getName(), first.getEmail())
        ));

        given(userService.findUserById(LOGIN_USER_DTO.getId())).willReturn(me);

        Set<UserResponseDto> actual = friendService.findFriendRequestsByUserId(LOGIN_USER_DTO.getId());
        assertEquals(expected, actual);
    }
}