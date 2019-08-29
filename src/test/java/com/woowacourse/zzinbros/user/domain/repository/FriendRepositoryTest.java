package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class FriendRepositoryTest extends BaseTest {
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    private UserRepository userRepository;
    private User user1;
    private User user2;
    private User user3;

    private Friend friend1;
    private Friend friend2;
    private Friend friend3;


    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("11", "a@a.com", "12345678"));
        user2 = userRepository.save(new User("22", "b@a.com", "12345678"));
        user3 = userRepository.save(new User("33", "c@a.com", "12345678"));

        friend1 = friendRepository.save(new Friend(user1, user2));
        friendRepository.save(new Friend(user2, user1));
        friend2 = friendRepository.save(new Friend(user2, user3));
        friendRepository.save(new Friend(user3, user2));
        friend3 = friendRepository.save(new Friend(user3, user1));
        friendRepository.save(new Friend(user1, user3));
        friendRepository.flush();
    }

    @Test
    void name() {
        assertThat(friendRepository.findAllByOwner(user1).size()).isEqualTo(2);
    }
}