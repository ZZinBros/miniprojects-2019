package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserAndFriendRepositoryTest extends UserBaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @Test
    @DisplayName("친구 요청 추가 후 Friend 객체에서 확인할 수 있다")
    void friendAddAndCheckFriend() {
        User me = userRepository.save(userSampleOf(SAMPLE_ONE));
        User first = userRepository.save(userSampleOf(SAMPLE_TWO));
        User second = userRepository.save(userSampleOf(SAMPLE_THREE));

        Friend firstFriend = friendRepository.save(Friend.of(me, first));
        Friend secondFriend = friendRepository.save(Friend.of(me, second));

        Set<Friend> expected = new HashSet<>(Arrays.asList(
                firstFriend,
                secondFriend
        ));
        Set<Friend> actualByMe = friendRepository.findBySender(me);
        Set<Friend> actualByFirst = friendRepository.findBySender(first);

        assertEquals(2, actualByMe.size());
        assertEquals(expected, actualByMe);
        assertEquals(0, actualByFirst.size());
    }

    @Test
    @DisplayName("친구 추가 후 User 객체에서 확인할 수 있다")
    void friendAddAndCheckUser() {
        User me = userRepository.save(userSampleOf(SAMPLE_ONE));
        User first = userRepository.save(userSampleOf(SAMPLE_TWO));
        User second = userRepository.save(userSampleOf(SAMPLE_THREE));

        friendRepository.save(Friend.of(me, first));
        friendRepository.save(Friend.of(first, me));
        friendRepository.save(Friend.of(me, second));

        User checkedMe = userRepository.findById(me.getId()).get();
        User checkedFirst = userRepository.findById(first.getId()).get();
        User checkedSecond = userRepository.findById(second.getId()).get();

        assertEquals(2, checkedMe.getFollowing().size());
        assertEquals(1, checkedMe.getFollowedBy().size());
        assertEquals(1, checkedFirst.getFollowing().size());
        assertEquals(1, checkedFirst.getFollowedBy().size());
        assertEquals(0, checkedSecond.getFollowing().size());
        assertEquals(1, checkedSecond.getFollowedBy().size());
    }

    @Test
    @DisplayName("중복된 Friend를 저장했을 때 예외가 발생한다")
    void friendAddWhenAlreadyExists() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User other = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));

        friendRepository.save(Friend.of(me, other));

        assertThatThrownBy(() -> friendRepository.save(Friend.of(me, other)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("중복된 Friend를 저장했을 때 True를 반환한다")
    void existsByFromAndToWhenAlreadyExists() {
        User me = userRepository.save(userSampleOf(SAMPLE_ONE));
        User other = userRepository.save(userSampleOf(SAMPLE_TWO));

        friendRepository.save(Friend.of(me, other));

        assertTrue(friendRepository.existsBySenderAndReceiver(me, other));
    }

    @Test
    @DisplayName("친구 관계인 회원이 탈퇴했을 때 나머지 회원은 유지된다")
    void deleteUser() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        friendRepository.save(Friend.of(me, first));
        friendRepository.save(Friend.of(first, me));

        userRepository.deleteById(me.getId());

        assertEquals(first, userRepository.findById(first.getId()).orElseThrow(IllegalArgumentException::new));
        assertThatThrownBy(() -> userRepository.findById(me.getId()).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("친구 관계인 회원이 탈퇴했을 때 나머지 회원은 유지된다")
    void deleteFriend() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        Friend deletedFriend = friendRepository.save(Friend.of(me, first));
        Friend savedFriend = friendRepository.save(Friend.of(first, me));

        friendRepository.deleteById(deletedFriend.getId());

        assertEquals(savedFriend, friendRepository.findById(savedFriend.getId())
                .orElseThrow(IllegalArgumentException::new));
        assertEquals(first, userRepository.findById(first.getId())
                .orElseThrow(IllegalArgumentException::new));
        assertEquals(me, userRepository.findById(me.getId())
                .orElseThrow(IllegalArgumentException::new));
    }
}