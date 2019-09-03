package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest extends UserBaseTest {

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
        User actual = userRepository.findByEmailEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("이메일이 중복일 때 True를 반환하는지 발생하는지 테스트")
    void signupWhenEmailExists() {
        User user = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        assertTrue(userRepository.existsUserByEmailEmail(user.getEmail()));
    }

    @Test
    @DisplayName("문자열을 포함한 페이징한 유저 목록을 조회한다")
    void searchWhenNameExists() {
        User user1 = userRepository.save(new User("name1", "name1@test.com", "123qweasd"));
        User user2 = userRepository.save(new User("name2", "name2@test.com", "123qweasd"));
        User user3 = userRepository.save(new User("name3", "name3@test.com", "123qweasd"));
        userRepository.save(new User("not", "name4@test.com", "123qweasd"));

        List<User> expected = Arrays.asList(user1, user2, user3);
        List<User> actual = userRepository.findByNameNameContaining("name", PageRequest.of(0, 3));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("틀린 검색어로 페이징한 유저 목록을 찾지 못한다")
    void searchWhenNameNotExists() {
        userRepository.save(new User("name1", "name1@test.com", "123qweasd"));
        userRepository.save(new User("name2", "name2@test.com", "123qweasd"));
        userRepository.save(new User("name3", "name3@test.com", "123qweasd"));

        List<User> expected = Collections.emptyList();
        List<User> actual = userRepository.findByNameNameContaining("not", PageRequest.of(0, 3));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("최근 가입한 순서대로 유저를 반환한다")
    void findRecentUsers() {
        User user1 = userRepository.save(new User("name1", "name1@test.com", "123qweasd"));
        User user2 = userRepository.save(new User("name2", "name2@test.com", "123qweasd"));
        User user3 = userRepository.save(new User("name3", "name3@test.com", "123qweasd"));

        List<User> expected = Arrays.asList(user3, user2, user1);
        List<User> actual = userRepository.findLatestUsers(PageRequest.of(0, 5));
        assertEquals(expected, actual);
    }
}