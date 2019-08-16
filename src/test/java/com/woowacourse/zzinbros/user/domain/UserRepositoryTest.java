package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
        userRepository.save(user);
    }

    @Test
    void findByEmail() {
        User actual = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertEquals(user, actual);
    }
}