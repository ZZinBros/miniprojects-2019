package com.woowacourse.zzinbros.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FriendControllerTest {

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    FriendController friendController;

    private UserResponseDto userResponseDto;
    private UserResponseDto loginUserDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController)
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();
        userResponseDto = new UserResponseDto(1L, UserTest.BASE_NAME, UserTest.BASE_EMAIL);
        loginUserDto = new UserResponseDto(2L, UserTest.BASE_NAME, "session@mail.com");
    }

    @Test
    @DisplayName("친구 추가 Post 요청을 제대로 수행하는지 Test")
    void friendPostTest() throws Exception {
        given(userService.addFriends(1L, 2L)).willReturn(true);
        given(userService.addFriends(2L, 1L)).willReturn(true);
        mockMvc.perform(post("/friends")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(userResponseDto))
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().isFound());
    }
}