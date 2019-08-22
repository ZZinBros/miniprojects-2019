package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.EmailAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest extends BaseTest {

    private static final long BASE_ID = 1L;
    private static final String MISMATCH_EMAIL = "error@test.com";

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private UserRequestDto userRequestDto;
    private UserUpdateDto userUpdateDto;
    private User user;
    private User notValidUser;
    private UserResponseDto validLoginUserDto;
    private UserResponseDto notValidLoginUserDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto(
                UserTest.BASE_NAME,
                UserTest.BASE_EMAIL,
                UserTest.BASE_PASSWORD);
        userUpdateDto = new UserUpdateDto(
                UserTest.BASE_NAME,
                UserTest.BASE_EMAIL
        );
        user = userRequestDto.toEntity();
        notValidUser = new User(UserTest.BASE_NAME, MISMATCH_EMAIL, UserTest.BASE_PASSWORD);
        validLoginUserDto = new UserResponseDto(BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        notValidLoginUserDto = new UserResponseDto(BASE_ID, UserTest.BASE_NAME, MISMATCH_EMAIL);
    }

    @Test
    @DisplayName("정상 회원 가입 테스트")
    void addUser() {
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(false);
        given(userRepository.save(user)).willReturn(user);

        User savedUser = userService.register(userRequestDto);
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("이미 이메일이 존재할 때 가입 실패")
    void failAddUserWhenUserExists() {
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(true);
        assertThatThrownBy(() ->
                userService.register(userRequestDto)).isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUser() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(validLoginUserDto.getEmail())).willReturn(Optional.ofNullable(user));

        User updatedUser = userService.modify(BASE_ID, userUpdateDto, validLoginUserDto);
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("다른 유저가 수정 시도할 때 에외 발생")
    void updateUserWhenNotValid() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(notValidLoginUserDto.getEmail())).willReturn(Optional.ofNullable(notValidUser));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userUpdateDto, notValidLoginUserDto))
                .isInstanceOf(NotValidUserException.class);
    }

    @Test
    @DisplayName("수정하려는 유저가 없을 때 예외 발생")
    void updateUserWhenUserNotExist() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userUpdateDto, notValidLoginUserDto))
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
        given(userRepository.findByEmail(validLoginUserDto.getEmail())).willReturn(Optional.ofNullable(user));

        userService.delete(BASE_ID, validLoginUserDto);
        verify(userRepository, times(1)).deleteById(BASE_ID);
    }

    @Test
    @DisplayName("다른 유저가 탈퇴 신청할 때 예외 발생")
    void deleteUserWhenUserNotMatch() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));

        assertThatThrownBy(() -> userService.delete(BASE_ID, notValidLoginUserDto));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        given(userRepository.findByEmail(userRequestDto.getEmail())).willReturn(Optional.ofNullable(user));

        UserResponseDto loginUserDto = userService.login(userRequestDto);
        assertThat(loginUserDto.getEmail()).isEqualTo(userRequestDto.getEmail());
        assertThat(loginUserDto.getName()).isEqualTo(userRequestDto.getName());
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

    @Test
    @DisplayName("로그인한 계정에 맞는 User 반환")
    void getUserBySession() {
        given(userRepository.findById(validLoginUserDto.getId())).willReturn(Optional.ofNullable(user));

        User actual = userService.findLoggedInUser(validLoginUserDto);

        assertThat(actual).isEqualTo(user);
    }

    @Test
    @DisplayName("친구의 목록을 반환 받기")
    void getFriendsOfTest() {
        Set<User> friends = new HashSet<>(Arrays.asList(
                new User(UserTest.BASE_NAME, "1@email.com", UserTest.BASE_PASSWORD),
                new User(UserTest.BASE_NAME, "2@email.com", UserTest.BASE_PASSWORD)
        ));
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByFriends(user)).willReturn(friends);

        Set<UserResponseDto> actual = userService.getFriendsOf(1L);
        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(null, UserTest.BASE_NAME, "1@email.com"),
                new UserResponseDto(null, UserTest.BASE_NAME, "2@email.com")
        ));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("친구 추가")
    void addTest() {
        User friendOne = new User(UserTest.BASE_NAME, "1@email.com", UserTest.BASE_PASSWORD);
        User friendTwo = new User(UserTest.BASE_NAME, "2@email.com", UserTest.BASE_PASSWORD);

        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(friendOne));
        given(userRepository.findById(2L)).willReturn(Optional.ofNullable(friendTwo));

        assertTrue(userService.addFriends(1L, 2L));
        assertTrue(userService.addFriends(2L, 1L));
        assertThat(friendOne.getCopyOfFriends()).contains(friendTwo);
        assertThat(friendTwo.getCopyOfFriends()).contains(friendOne);
    }
}
