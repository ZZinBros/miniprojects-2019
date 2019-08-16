package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import com.woowacourse.zzinbros.user.web.support.UserControllerExceptionAdvice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class UserEditPageControllerTest {

    MockMvc mockMvc;

    @Mock
    UserService userService;

    @InjectMocks
    UserEditPageController userEditPageController;

    private UserSession userSession;
    private User user;

    @BeforeEach
    void setUp() {
        userSession = new UserSession(UserControllerTest.BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        user = new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
        mockMvc = MockMvcBuilders.standaloneSetup(userEditPageController)
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .setControllerAdvice(new UserControllerExceptionAdvice())
                .build();
    }

    @Test
    @DisplayName("사용자가 유저 수정 페이지로 이동")
    void showEditPageTest() throws Exception {
        mockMvc.perform(get("/users/" + UserControllerTest.BASE_ID + "/edit")
                .sessionAttr(UserSession.LOGIN_USER, userSession))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인가되지 않은 사용자가 유저 수정 페이지로 이동 불가")
    void showEditPageWhenUserMismatchTest() throws Exception {
        mockMvc.perform(get("/users/" + UserControllerTest.BASE_ID + "/edit"))
                .andExpect(status().is3xxRedirection());
    }
}