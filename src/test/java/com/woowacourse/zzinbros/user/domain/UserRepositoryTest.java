package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    private final static Map<Integer, User> SAMPLE_USERS;
    private final static int SAMPLE_ONE = 1;
    private final static int SAMPLE_TWO = 2;
    private final static int SAMPLE_THREE = 3;
    private final static int SAMPLE_FOUR = 4;

    static {
        SAMPLE_USERS = new HashMap<>();
        SAMPLE_USERS.put(SAMPLE_ONE, new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_TWO, new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_THREE, new User(UserTest.BASE_NAME, "3@mail.com", UserTest.BASE_PASSWORD));
        SAMPLE_USERS.put(SAMPLE_FOUR, new User(UserTest.BASE_NAME, "4@mail.com", UserTest.BASE_PASSWORD));
    }

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일이 중복이 아닐때 False를 반환하는지 발생하는지 테스트")
    void findByEmail() {
        User user = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User actual = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("이메일이 중복일 때 True를 반환하는지 발생하는지 테스트")
    void signupWhenEmailExists() {
        User user = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        assertTrue(userRepository.existsUserByEmail(user.getEmail()));
    }

    @Test
    @DisplayName("친구 추가가 잘되는지 테스트")
    void addFriends() {
        User one = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User two = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));

        one.addFriend(two);
        two.addFriend(one);

        User actual = userRepository.findByEmail(one.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        User actualTwo = userRepository.findByEmail(two.getEmail())
                .orElseThrow(IllegalArgumentException::new);

        assertEquals(1, actual.getCopyOfFriends().size());
        assertEquals(1, one.getCopyOfFriends().size());
        assertEquals(1, two.getCopyOfFriends().size());
        assertEquals(1, actualTwo.getCopyOfFriends().size());
    }

    @Test
    @DisplayName("해당 유저의 친구 목록 반환하는지 테스트")
    void friendsList() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User friendOne = userRepository.save(SAMPLE_USERS.get(SAMPLE_TWO));
        User friendTwo = userRepository.save(SAMPLE_USERS.get(SAMPLE_THREE));
        User notFriend = userRepository.save(SAMPLE_USERS.get(SAMPLE_FOUR));

        me.addFriend(friendOne);
        friendOne.addFriend(me);

        me.addFriend(friendTwo);
        friendTwo.addFriend(me);

        friendTwo.addFriend(notFriend);
        notFriend.addFriend(friendTwo);

        assertThat(userRepository.findByFriends(me)).contains(friendOne, friendTwo);
        assertEquals(userRepository.findByFriends(me).size(), 2);
    }

    @Test
    @DisplayName("친구 관계를 맺은 상태에서 회원 삭제 테스트")
    void signOutWhenHasFriendTest() {
        User me = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        User friend = userRepository.save(SAMPLE_USERS.get(SAMPLE_THREE));

        me.addFriend(friend);
        friend.addFriend(me);

        assertEquals(1, userRepository.findByFriends(me).size());

        userRepository.delete(friend);

        assertEquals(0, userRepository.findByFriends(me).size());
    }
}