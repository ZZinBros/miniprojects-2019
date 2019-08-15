package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserRepository;
import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    private static final long BASE_ID = 1L;
    private static final String MISMATCH_EMAIL = "error@test.com";
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserRequestDto userRequestDto;
    private User user;
    private User notValidUser;
    private UserSession validUserSession;
    private UserSession notValidUserSession;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                UserTest.BASE_NAME,
                UserTest.BASE_EMAIL,
                UserTest.BASE_PASSWORD);
        user = userRequestDto.toEntity();
        notValidUser = new User(UserTest.BASE_NAME, MISMATCH_EMAIL, UserTest.BASE_PASSWORD);
        validUserSession = new UserSession(BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        notValidUserSession = new UserSession(BASE_ID, UserTest.BASE_NAME, MISMATCH_EMAIL);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    void addUser() {
        given(userRepository.save(user)).willReturn(user);

        User savedUser = userService.register(userRequestDto);
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("이미 이메일이 존재할 때 가입 실패")
    void failAddUserWhenUserExists() {
        given(userRepository.save(userRequestDto.toEntity())).willThrow(UserAlreadyExistsException.class);
        assertThatThrownBy(() ->
                userService.register(userRequestDto)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUser() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(validUserSession.getEmail())).willReturn(Optional.ofNullable(user));

        User updatedUser = userService.modify(BASE_ID, userRequestDto, validUserSession);
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("다른 유저가 수정 시도할 때 에외 발생")
    void updateUserWhenNotValid() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(notValidUserSession.getEmail())).willReturn(Optional.ofNullable(notValidUser));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userRequestDto, notValidUserSession))
                .isInstanceOf(NotValidUserException.class);
    }

    @Test
    @DisplayName("수정하려는 유저가 없을 때 예외 발생")
    void updateUserWhenUserNotExist() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userRequestDto, notValidUserSession))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("회원 정보 조회")
    void findUser() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));

        assertThat(userService.findUserById(BASE_ID)).isEqualTo(user);
    }

    @Test
    @DisplayName("회원 정보 ID로 삭제")
    void deleteUser() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(validUserSession.getEmail())).willReturn(Optional.ofNullable(user));

        userService.delete(BASE_ID, validUserSession);
        verify(userRepository, times(1)).deleteById(BASE_ID);
    }

    @Test
    @DisplayName("다른 유저가 탈퇴 신청할 때 예외 발생")
    void deleteUserWhenUserNotMatch() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));

        assertThatThrownBy(() -> userService.delete(BASE_ID, notValidUserSession));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        given(userRepository.findByEmail(userRequestDto.getEmail())).willReturn(Optional.ofNullable(user));

        UserSession userSession = userService.login(userRequestDto);
        assertThat(userSession.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(userSession.getName()).isEqualTo(userRequestDto.getName());
    }

    @Test
    @DisplayName("비밀번호가 다를 때 로그인 실패")
    void loginFail() {
        given(userRepository.findByEmail(userRequestDto.getEmail())).willReturn(Optional.ofNullable(user));

        UserRequestDto loginRequestDto
                = new UserRequestDto(user.getName(), user.getEmail(), user.getPassword() + "a");
        assertThatThrownBy(() -> userService.login(loginRequestDto))
                .isInstanceOf(UserLoginException.class);
    }
}