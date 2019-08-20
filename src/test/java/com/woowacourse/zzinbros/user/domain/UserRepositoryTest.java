package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest extends BaseTest {

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
        userRepository.deleteAll();
    }

    @Test
    void findByEmail() {
        userRepository.save(user);
        User actual = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("친구 추가가 잘되는지 테스트")
    void friends() {
        User one = userRepository.save(new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        User two = userRepository.save(new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));

        one.addFriend(two);

        userRepository.save(one);
        User actual = userRepository.findByEmail(one.getEmail()).orElseThrow(IllegalArgumentException::new);
        userRepository.save(two);
        User actualTwo = userRepository.findByEmail(two.getEmail()).orElseThrow(IllegalArgumentException::new);

        assertEquals(1, actual.getFriends().size());
        assertEquals(1, one.getFriends().size());
        assertEquals(1, two.getFriends().size());
        assertEquals(1, actualTwo.getFriends().size());
    }

    @Test
    @DisplayName("해당 유저의 친구 목록 반환하는지 테스트")
    void friendsList() {
        User me = userRepository.save(new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        User friendOne = userRepository.save(new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));
        User friendTwo = userRepository.save(new User(UserTest.BASE_NAME, "3@mail.com", UserTest.BASE_PASSWORD));
        User notFriend = userRepository.save(new User(UserTest.BASE_NAME, "4@mail.com", UserTest.BASE_PASSWORD));

        me.addFriend(friendOne);
        me.addFriend(friendTwo);
        friendTwo.addFriend(notFriend);

        assertThat(userRepository.findByFriends(me)).contains(friendOne, friendTwo);
        assertEquals(userRepository.findByFriends(me).size(), 2);
    }
}