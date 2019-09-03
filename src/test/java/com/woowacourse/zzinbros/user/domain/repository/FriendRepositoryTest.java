package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class FriendRepositoryTest extends BaseTest {
    @Autowired
    FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = userRepository.save(new User("11", "a@a.com", "12345678"));
        user2 = userRepository.save(new User("22", "b@a.com", "12345678"));
        user3 = userRepository.save(new User("33", "c@a.com", "12345678"));
    }

    @Test
    @DisplayName("owner와 slave로 친구 삭제")
    void deleteByOwnerAndSlave() {
        friendRepository.deleteByOwnerAndSlave(user2, user3);
        friendRepository.deleteByOwnerAndSlave(user3, user2);
        assertFalse(friendRepository.existsByOwnerAndSlave(user2, user3));
        assertFalse(friendRepository.existsByOwnerAndSlave(user3, user2));
    }

    @Test
    @DisplayName("owner로 친구를 조회한다")
    void test() {
        friendRepository.save(new Friend(user2, user1));
        friendRepository.save(new Friend(user2, user3));

        Set<User> expected = new HashSet<>(Arrays.asList(user1, user3));
        Set<User> actual = friendRepository.findSlavesByOwner(user2);
        assertEquals(expected, actual);
    }
}