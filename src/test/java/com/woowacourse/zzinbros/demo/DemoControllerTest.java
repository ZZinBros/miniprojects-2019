package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class DemoControllerTest {
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext context;

    @Autowired
    DemoController demoController;

    @BeforeAll
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("로그인 안됏을 때 /entrance로 redirect한다")
    void demoWhenNotLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("로그인 됐을 때 index로 이동")
    void demoWhenLogin() throws Exception {
        UserResponseDto loginUserDto = new UserResponseDto(1L, "name", "login@test.com");
        mockMvc.perform(get("/")
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().isOk());
    }

    @Test
    void enter() throws Exception {
        mockMvc.perform(get("/entrance"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("가입하기")));
    }
}