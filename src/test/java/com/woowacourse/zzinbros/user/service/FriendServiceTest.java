package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.domain.repository.UserBaseTest;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FriendServiceTest extends UserBaseTest {

    @MockBean
    private FriendRepository friendRepository;

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
    void requestFailWhenUserExistsTest() {
        Friend friend = Friend.of(me, other);
        given(friendRepository.save(friend)).willReturn(friend);
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(true);

        assertThatThrownBy(() -> friendService.sendFriendRequest(me, other))
                .isInstanceOf(AlreadyFriendRequestExist.class);
    }

    @Test
    @DisplayName("쌍방으로 친구 신청이 되있을 때 True 반환")
    void checkFriendIfFriendTest() {
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(true);
        given(friendRepository.existsByFromAndTo(other, me)).willReturn(true);

        assertTrue(friendService.checkFriend(me, other));
    }

    @Test
    @DisplayName("쌍방으로 친구 신청이 안되있을 때 False 반환")
    void checkFriendIfNotFriendTest() {
        given(friendRepository.existsByFromAndTo(me, other)).willReturn(true);
        given(friendRepository.existsByFromAndTo(other, me)).willReturn(false);

        assertFalse(friendService.checkFriend(me, other));
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
}