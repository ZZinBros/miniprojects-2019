package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.FriendRequest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class UserAndFriendRepositoryTest extends UserBaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Test
    @DisplayName("중복된 Friend를 저장했을 때 예외가 발생한다")
    void friendAddWhenAlreadyExists() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User other = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));

        friendRequestRepository.save(new FriendRequest(me, other));

        assertThatThrownBy(() -> friendRequestRepository.save(new FriendRequest(me, other)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("중복된 Friend를 저장했을 때 True를 반환한다")
    void existsByFromAndToWhenAlreadyExists() {
        User me = userRepository.save(userSampleOf(SAMPLE_ONE));
        User other = userRepository.save(userSampleOf(SAMPLE_TWO));

        friendRequestRepository.save(new FriendRequest(me, other));

        assertTrue(friendRequestRepository.existsBySenderAndReceiver(me, other));
    }

    @Test
    @DisplayName("친구 관계인 회원이 탈퇴했을 때 나머지 회원은 유지된다")
    void deleteUser() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        friendRequestRepository.save(new FriendRequest(me, first));
        friendRequestRepository.save(new FriendRequest(first, me));

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
        FriendRequest deletedFriend = friendRequestRepository.save(new FriendRequest(me, first));
        FriendRequest savedFriend = friendRequestRepository.save(new FriendRequest(first, me));

        friendRequestRepository.deleteById(deletedFriend.getId());

        assertEquals(savedFriend, friendRequestRepository.findById(savedFriend.getId())
                .orElseThrow(IllegalArgumentException::new));
        assertEquals(first, userRepository.findById(first.getId())
                .orElseThrow(IllegalArgumentException::new));
        assertEquals(me, userRepository.findById(me.getId())
                .orElseThrow(IllegalArgumentException::new));
    }
}