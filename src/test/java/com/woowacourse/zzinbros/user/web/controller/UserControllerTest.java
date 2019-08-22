package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.EmailAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.LoginSessionManager;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import com.woowacourse.zzinbros.user.web.support.UserControllerExceptionAdvice;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest extends BaseTest {
    static final long BASE_ID = 1L;
    static final long MISMATCH_ID = 2L;

    MockMvc mockMvc;

    @Mock
    UserService userService;

    @Mock
    LoginSessionManager loginSessionManager;

    @InjectMocks
    UserController userController;

    private User user;
    private UserRequestDto userRequestDto;
    private UserUpdateDto userUpdateDto;
    private UserResponseDto loginUserDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new UserControllerExceptionAdvice())
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();
        userRequestDto = new UserRequestDto(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
        userUpdateDto = new UserUpdateDto(UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        user = new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
        loginUserDto = new UserResponseDto(BASE_ID, user.getName(), user.getEmail());
    }

    @Test
    @DisplayName("정상적으로 회원가입")
    void postTest() throws Exception {
        given(userService.register(userRequestDto))
                .willReturn(user);

        mockMvc.perform(post("/users")
                .param("name", userRequestDto.getName())
                .param("email", userRequestDto.getEmail())
                .param("password", userRequestDto.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("중복된 이메일이 존재해서 회원가입 실패")
    void postWhenUserExistsTest() throws Exception {
        given(userService.register(userRequestDto))
                .willThrow(EmailAlreadyExistsException.class);

        mockMvc.perform(post("/users")
                .param("name", userRequestDto.getName())
                .param("email", userRequestDto.getEmail())
                .param("password", userRequestDto.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound());
    }

    @Test
    @DisplayName("정상적으로 회원 정보 변경")
    void putTest() throws Exception {
        given(userService.modify(BASE_ID, userUpdateDto, loginUserDto))
                .willReturn(user);

        mockMvc.perform(put("/users/" + BASE_ID)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", userUpdateDto.getName())
                .param("email", userUpdateDto.getEmail()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("로그인 한 유저와 변경하려는 유저 정보가 다를 때 변경 실패")
    void putTestWhenUserMismatch() throws Exception {
        given(userService.modify(MISMATCH_ID, userUpdateDto, loginUserDto))
                .willThrow(NotValidUserException.class);

        mockMvc.perform(put("/users/" + MISMATCH_ID)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", userUpdateDto.getName())
                .param("email", userUpdateDto.getEmail()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("회원 정보가 없을 때 회원 정보 변경 실패")
    void putWhenUserNotFoundTest() throws Exception {
        given(userService.modify(BASE_ID, userUpdateDto, loginUserDto))
                .willThrow(UserNotFoundException.class);

        mockMvc.perform(put("/users/" + BASE_ID)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto)
                .param("name", userUpdateDto.getName())
                .param("email", userUpdateDto.getEmail()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("정상적으로 회원 정보 조회")
    void getTest() throws Exception {
        given(userService.findUserById(BASE_ID))
                .willReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + BASE_ID))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원 정보가 없을 떄 회원 정보 조회 실패")
    void getWhenUserNotFoundTest() throws Exception {
        given(userService.findUserById(BASE_ID))
                .willThrow(UserNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + BASE_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("정상적으로 회원 정보 삭제")
    void deleteTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + BASE_ID)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().isFound());

        verify(userService, times(1)).delete(BASE_ID, loginUserDto);
    }
}