package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.web.support.UserSession;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import com.woowacourse.zzinbros.user.web.support.UserControllerExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LogoutControllerTest {

    MockMvc mockMvc;

    @InjectMocks
    LogoutController logoutController;

    private UserSession userSession;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(logoutController)
                .setControllerAdvice(new UserControllerExceptionAdvice())
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();
        userSession = new UserSession(UserControllerTest.BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
    }

    @Test
    @DisplayName("정상 로그아웃 테스트")
    void logoutTestWhenLogin() throws Exception {
        mockMvc.perform(get("/logout")
                .sessionAttr(UserSession.LOGIN_USER, userSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 안했을 때 로그아웃 요청하면 인덱스 페이지로 redirect")
    void logoutTestWhenNotLogin() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isFound());
    }
}