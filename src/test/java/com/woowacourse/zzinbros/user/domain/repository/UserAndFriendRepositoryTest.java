package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserAndFriendRepositoryTest extends UserBaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @Test
    @DisplayName("정상적으로 친구 추가 후 조회 테스트")
    void friendAddTest() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        User second = userRepository.save(SAMPLE_USERS.get(SAMPLE_THREE));

        Friend firstFriend = friendRepository.save(Friend.of(me, first));
        Friend secondFriend = friendRepository.save(Friend.of(me, second));

        Set<Friend> expected = new HashSet<>(Arrays.asList(
                firstFriend,
                secondFriend
        ));
        Set<Friend> actualByMe = friendRepository.findByFrom(me);
        Set<Friend> actualByFirst = friendRepository.findByFrom(first);

        assertEquals(2, actualByMe.size());
        assertEquals(expected, actualByMe);
        assertEquals(0, actualByFirst.size());
    }

    @Test
    @DisplayName("중복된 Friend를 저장했을 때 예외가 발생하는지 확인")
    void friendAddWhenAlreadyExists() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User other = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));

        friendRepository.save(Friend.of(me, other));

        assertThatThrownBy(() -> friendRepository.save(Friend.of(me, other)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @DisplayName("중복된 Friend를 저장했을 때 True를 반환하는지 확인")
    void existsByFromAndToWhenAlreadyExists() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User other = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));

        friendRepository.save(Friend.of(me, other));

        assertTrue(friendRepository.existsByFromAndTo(me, other));
    }

    @Test
    @DisplayName("친구 관계가 설정됬을 때 회원 삭제를 했을 경우")
    void deleteTest() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        friendRepository.save(Friend.of(me, first));

        userRepository.deleteById(me.getId());

        assertEquals(first, userRepository.findById(first.getId()).orElseThrow(IllegalArgumentException::new));
        assertThatThrownBy(() -> userRepository.findById(me.getId()).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}