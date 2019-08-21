package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

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
    @DisplayName("이메일이 중복일 때 에러가 발생하는지 테스트")
    void signupWhenEmailExists() {
        userRepository.save(user);

        assertTrue(userRepository.existsUserByEmail(user.getEmail()));
    }

    @Test
    @DisplayName("친구 추가가 잘되는지 테스트")
    void addFriends() {
        User one = userRepository.save(new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        User two = userRepository.save(new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));

        one.addFriend(two);
        two.addFriend(one);

        User actual = userRepository.findByEmail(one.getEmail()).orElseThrow(IllegalArgumentException::new);
        User actualTwo = userRepository.findByEmail(two.getEmail()).orElseThrow(IllegalArgumentException::new);

        assertEquals(1, actual.getCopyOfFriends().size());
        assertEquals(1, one.getCopyOfFriends().size());
        assertEquals(1, two.getCopyOfFriends().size());
        assertEquals(1, actualTwo.getCopyOfFriends().size());
    }

    @Test
    @DisplayName("해당 유저의 친구 목록 반환하는지 테스트")
    void friendsList() {
        User me = userRepository.save(new User(UserTest.BASE_NAME, "1@mail.com", UserTest.BASE_PASSWORD));
        User friendOne = userRepository.save(new User(UserTest.BASE_NAME, "2@mail.com", UserTest.BASE_PASSWORD));
        User friendTwo = userRepository.save(new User(UserTest.BASE_NAME, "3@mail.com", UserTest.BASE_PASSWORD));
        User notFriend = userRepository.save(new User(UserTest.BASE_NAME, "4@mail.com", UserTest.BASE_PASSWORD));

        me.addFriend(friendOne);
        friendOne.addFriend(me);

        me.addFriend(friendTwo);
        friendTwo.addFriend(me);

        friendTwo.addFriend(notFriend);
        notFriend.addFriend(friendTwo);

        assertThat(userRepository.findByFriends(me)).contains(friendOne, friendTwo);
        assertEquals(userRepository.findByFriends(me).size(), 2);
    }
}