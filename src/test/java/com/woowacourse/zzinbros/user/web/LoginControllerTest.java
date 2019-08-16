package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

    MockMvc mockMvc;

    @Mock
    UserService userService;

    @InjectMocks
    LoginController loginController;

    private UserRequestDto userRequestDto;
    private UserSession userSession;

    @BeforeEach
    void setUp() {
        userSession = new UserSession(UserControllerTest.BASE_ID, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        userRequestDto = new UserRequestDto(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .alwaysDo(print())
                .build();
    }

    @Test
    void loginSuccess() throws Exception {
        given(userService.login(userRequestDto)).willReturn(userSession);

        String url = mockMvc.perform(post("/login")
                .param("name", userRequestDto.getName())
                .param("email", userRequestDto.getEmail())
                .param("password", userRequestDto.getPassword()))
                .andExpect(status().isFound())
                .andReturn().getResponse().getHeader("Location");

        assertTrue(url.equals("/"));
    }

    @Test
    void loginFail() throws Exception {
        given(userService.login(userRequestDto))
                .willThrow(UserLoginException.class);

        String url = mockMvc.perform(post("/login")
                .param("name", userRequestDto.getName())
                .param("email", userRequestDto.getEmail())
                .param("password", userRequestDto.getPassword()))
                .andExpect(status().isFound())
                .andReturn().getResponse().getHeader("Location");

        assertTrue(url.equals("/login"));
    }
}