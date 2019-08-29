package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.common.config.upload.UploadTo;
import com.woowacourse.zzinbros.common.config.upload.UploadToLocal;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.mediafile.service.MediaFileService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class UserServiceTest extends BaseTest {

    private static final long BASE_ID = 1L;
    private static final String MISMATCH_EMAIL = "error@test.com";

    @Mock
    MediaFileService mediaFileService;

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
    private UploadTo DEFAULT_PROFILE = new UploadToLocal(null, "down", "up");
    private MediaFile mediaFile = new MediaFile("/images/some.png");

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
        user = userRequestDto.toEntity(mediaFile);
        notValidUser = new User(UserTest.BASE_NAME, MISMATCH_EMAIL, UserTest.BASE_PASSWORD);
        validLoginUserDto = new UserResponseDto(BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL, mediaFile.getUrl());
        notValidLoginUserDto = new UserResponseDto(BASE_ID, UserTest.BASE_NAME, MISMATCH_EMAIL, mediaFile.getUrl());
    }

    @Test
    @DisplayName("정상 회원 가입 테스트")
    void addUser() {
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(false);
        given(userRepository.save(user)).willReturn(user);
        given(mediaFileService.register(any())).willReturn(mediaFile);

        User savedUser = userService.register(userRequestDto, DEFAULT_PROFILE);
        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    @DisplayName("이미 이메일이 존재할 때 가입 실패")
    void failAddUserWhenUserExists() {
        given(userRepository.existsUserByEmail(user.getEmail())).willReturn(true);
        assertThatThrownBy(() ->
                userService.register(userRequestDto, DEFAULT_PROFILE)).isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateUser() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(validLoginUserDto.getEmail())).willReturn(Optional.ofNullable(user));

        User updatedUser = userService.modify(BASE_ID, userUpdateDto, validLoginUserDto, DEFAULT_PROFILE);
        assertThat(updatedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("다른 유저가 수정 시도할 때 에외 발생")
    void updateUserWhenNotValid() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(user));
        given(userRepository.findByEmail(notValidLoginUserDto.getEmail())).willReturn(Optional.ofNullable(notValidUser));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userUpdateDto, notValidLoginUserDto, DEFAULT_PROFILE))
                .isInstanceOf(NotValidUserException.class);
    }

    @Test
    @DisplayName("수정하려는 유저가 없을 때 예외 발생")
    void updateUserWhenUserNotExist() {
        given(userRepository.findById(BASE_ID)).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> userService.modify(BASE_ID, userUpdateDto, notValidLoginUserDto, DEFAULT_PROFILE))
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
}
