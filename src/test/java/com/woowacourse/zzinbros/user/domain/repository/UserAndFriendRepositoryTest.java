package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAndFriendRepositoryTest extends UserBaseTest {

    //@TODO 테스트 완료시 제거 할 것
//    @Autowired
//    UserRepository userRepository;
//
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @BeforeEach
    void setUp() {
//        friendRepository.deleteAll();
//        userRepository.deleteAll();
    }

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

//    @Test
//    @DisplayName("정상적으로 친구 추가했을 때 User에서 친구를 확인할 수 있는지 확인")
//    void test() {
//        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
//        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
//        TempUser second = userRepository.save(SAMPLE_USERS.get(SAMPLE_THREE));
//
//        Friend firstFriend = friendRepository.save(Friend.of(me, first));
//        Friend secondFriend = friendRepository.save(Friend.of(me, second));
//
//        Set<Friend> expected = new HashSet<>(Arrays.asList(
//                firstFriend,
//                secondFriend
//        ));
//
//        TempUser findMe = userRepository.findById(me.getId())
//                .orElseThrow(IllegalArgumentException::new);
//
//        TempUser findOther = userRepository.findById(first.getId())
//                .orElseThrow(IllegalArgumentException::new);
//
//        assertEquals(2, me.getFriends().size());
//        assertEquals(expected, findMe.getFriends());
//        assertEquals(0, findOther.getFriends().size());
//    }

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
//    @Test
//    @DisplayName("서로 친구 추가가 되었을 때 서로 친구인지 확인")
//    void checkIsFollowTo() {
//        TempUser me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
//        TempUser first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
//
//        friendRepository.save(Friend.of(me, first));
//        friendRepository.save(Friend.of(first, me));
//
//        assertTrue(me.isFollowTo(first));
//        assertTrue(first.isFollowTo(me));
//    }

//    @Test
//    @DisplayName("한쪽만 일방적으로 친구 신청했을 때 테스트")
//    void checkIsFollowToUnlessEachFollows() {
//        TempUser me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
//        TempUser first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
//
//        friendRepository.save(Friend.of(me, first));
//
//        assertTrue(me.isFollowTo(first));
//        assertFalse(first.isFollowTo(me));
//    }

    @Test
    @DisplayName("친구 관계가 설정됬을 때 회원 삭제를 했을 경우")
    void deleteTest() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User first = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        friendRepository.save(Friend.of(me, first));

        userRepository.deleteById(me.getId());

        //@TODO 물어보기
//        assertEquals(0, friendRepository.findAll());
        assertEquals(first, userRepository.findById(first.getId()).orElseThrow(IllegalArgumentException::new));
        assertThatThrownBy(() -> userRepository.findById(me.getId()).orElseThrow(IllegalArgumentException::new))
                .isInstanceOf(IllegalArgumentException.class);
    }
}