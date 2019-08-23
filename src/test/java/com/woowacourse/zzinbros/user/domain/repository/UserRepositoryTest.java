package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        User actual = userRepository.findByEmail(user.getEmail()).orElseThrow(IllegalArgumentException::new);
        assertEquals(user, actual);
    }

    @Test
    @DisplayName("이메일이 중복일 때 True를 반환하는지 발생하는지 테스트")
    void signupWhenEmailExists() {
        User user = userRepository.save(SAMPLE_USERS.get(SAMPLE_ONE));
        assertTrue(userRepository.existsUserByEmail(user.getEmail()));
    }
}