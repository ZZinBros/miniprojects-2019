package com.woowacourse.zzinbros.user;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserRequestDto userRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                UserTest.BASE_NAME,
                UserTest.BASE_EMAIL,
                UserTest.BASE_PASSWORD);
        user = userRequestDto.toEntity();
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void addUser() {
        given(userRepository.save(user)).willReturn(user);

        User savedUser = userService.add(userRequestDto);
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("회원 정보 ID로 수정")
    void updateUser() {
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        User updatedUser = userService.update(1L, userRequestDto);
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 정보 조회")
    void findUser() {
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));

        assertThat(userService.findUserById(1L)).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 정보 ID로 삭제")
    void deleteUser() {
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}