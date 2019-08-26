package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LogoutControllerTest extends BaseTest {

    MockMvc mockMvc;

    @Mock
    LoginSessionManager loginSessionManager;

    @InjectMocks
    LogoutController logoutController;

    private UserResponseDto loginUserDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(logoutController)
                .setControllerAdvice(new UserControllerExceptionAdvice())
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();
        loginUserDto = new UserResponseDto(UserControllerTest.BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
    }

    @Test
    @DisplayName("정상 로그아웃 테스트")
    void logoutTestWhenLogin() throws Exception {
        mockMvc.perform(post("/logout")
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("로그인 안했을 때 로그아웃 요청하면 인덱스 페이지로 redirect")
    void logoutTestWhenNotLogin() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().isFound());
    }
}