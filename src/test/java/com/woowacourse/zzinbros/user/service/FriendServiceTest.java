package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.FriendRequest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.domain.repository.FriendRequestRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FriendServiceTest extends UserBaseTest {

    @MockBean
    private FriendRequestRepository friendRequestRepository;

    @MockBean
    private FriendRepository friendRepository;

    @MockBean
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @BeforeEach
    void setUp() {
        given(userService.findUserById(1L)).willReturn(userSampleOf(SAMPLE_ONE));
    }

    @Test
    @DisplayName("친구 요청을 제대로 반환하는지")
    void findFriendRequestsByUserId() {
        Set<User> expected = new HashSet<>(Arrays.asList(userSampleOf(SAMPLE_TWO), userSampleOf(SAMPLE_ONE)));
        given(friendRequestRepository.findSendersByReceiver(userSampleOf(SAMPLE_ONE))).willReturn(expected);

        Set<UserResponseDto> actual = friendService.findFriendRequestsByUserId(1L);

        assertThat(actual).isEqualTo(friendService.friendRequestToUserResponseDto(expected));
    }

    @Test
    @DisplayName("친구 요청을 제대로 수행하는지")
    void sendFriendRequest() {
        given(friendRequestRepository.existsBySenderAndReceiver(any(User.class), any(User.class))).willReturn(false);
        friendService.registerFriend(LOGIN_USER_DTO, FRIEND_REQUEST_DTO);
        verify(friendRequestRepository, times(1)).save(any(FriendRequest.class));
    }

    @Test
    @DisplayName("친구 목록을 제대로 반환")
    void findFriendsByUser() {
        Set<User> users = new HashSet<>(Arrays.asList(userSampleOf(SAMPLE_TWO)));
        given(friendRepository.findSlavesByOwner(userSampleOf(SAMPLE_ONE))).willReturn(users);

        assertThat(friendService.findFriendsByUserId(1L)).isEqualTo(friendService.friendToUserResponseDto(users));
    }

    @Test
    @DisplayName("친구를 제대로 변환하는지")
    void friendToUserResponseDto() {
        User user1 = mockingId(userSampleOf(SAMPLE_ONE), 99L);
        User user2 = mockingId(userSampleOf(SAMPLE_ONE), 100L);
        Set<User> users = new HashSet<>();

        users.add(user1);
        users.add(user2);

        Set<UserResponseDto> expected = new HashSet<>();
        expected.add(new UserResponseDto(user1));
        expected.add(new UserResponseDto(user2));


        assertThat(friendService.friendToUserResponseDto(users)).isEqualTo(expected);
    }

    @Test
    @DisplayName("친구 요청을 제대로 변환하는지")
    void friendRequestToUserResponseDto() {
        Set<User> friends = new HashSet<>();

        friends.add(userSampleOf(SAMPLE_TWO));
        friends.add(userSampleOf(SAMPLE_THREE));

        Set<UserResponseDto> expected = new HashSet<>();
        expected.add(new UserResponseDto(userSampleOf(SAMPLE_TWO)));
        expected.add(new UserResponseDto(userSampleOf(SAMPLE_THREE)));

        assertThat(friendService.friendRequestToUserResponseDto(friends)).isEqualTo(expected);
    }

    @Test
    @DisplayName("친구 삭제")
    void deleteFriends() {
        User owner = userSampleOf(SAMPLE_ONE);
        User friend = userSampleOf(SAMPLE_TWO);
        given(userService.findLoggedInUser(LOGIN_USER_DTO)).willReturn(owner);
        given(userService.findUserById(2L)).willReturn(friend);

        friendService.deleteFriends(LOGIN_USER_DTO, 2L);

        verify(friendRepository).deleteByOwnerAndSlave(owner, friend);
        verify(friendRepository).deleteByOwnerAndSlave(friend, owner);
    }
}