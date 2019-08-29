package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@AutoConfigureMockMvc
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
        Set<FriendRequest> expected = new HashSet<>(Arrays.asList(new FriendRequest(userSampleOf(SAMPLE_TWO), userSampleOf(SAMPLE_ONE))));
        given(friendRequestRepository.findAllByReceiver(userSampleOf(SAMPLE_ONE))).willReturn(expected);

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
        Set<Friend> friends = new HashSet<>(Arrays.asList(new Friend(userSampleOf(1), userSampleOf(2))));
        given(friendRepository.findAllByOwner(userSampleOf(1))).willReturn(friends);

        assertThat(friendService.findFriendsByUserId(1L)).isEqualTo(friendService.friendToUserResponseDto(friends));
    }

    @Test
    @DisplayName("친구를 제대로 변환하는지")
    void friendToUserResponseDto() {
        Set<Friend> friends = new HashSet<>();

        friends.add(mockingId(new Friend(userSampleOf(1), userSampleOf(2)), 99L));
        friends.add(mockingId(new Friend(userSampleOf(2), userSampleOf(3)), 100L));

        Set<UserResponseDto> expected = new HashSet<>();
        expected.add(new UserResponseDto(userSampleOf(2).getId(), userSampleOf(2).getName(), userSampleOf(2).getEmail()));
        expected.add(new UserResponseDto(userSampleOf(3).getId(), userSampleOf(3).getName(), userSampleOf(3).getEmail()));


        assertThat(friendService.friendToUserResponseDto(friends)).isEqualTo(expected);
    }

    @Test
    @DisplayName("친구 요청을 제대로 변환하는지")
    void friendRequestToUserResponseDto() {
        Set<FriendRequest> friends = new HashSet<>();

        friends.add(mockingId(new FriendRequest(userSampleOf(1), userSampleOf(2)), 99L));
        friends.add(mockingId(new FriendRequest(userSampleOf(3), userSampleOf(2)), 100L));

        Set<UserResponseDto> expected = new HashSet<>();
        expected.add(new UserResponseDto(userSampleOf(1).getId(), userSampleOf(1).getName(), userSampleOf(1).getEmail()));
        expected.add(new UserResponseDto(userSampleOf(3).getId(), userSampleOf(3).getName(), userSampleOf(3).getEmail()));

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