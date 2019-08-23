package com.woowacourse.zzinbros.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.service.FriendService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class FriendControllerTest extends UserBaseTest {

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    FriendService friendService;

    @Autowired
    FriendController friendController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController)
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("친구 추가 Post 요청을 제대로 수행하는지 Test")
    void friendPostSuccessTest() throws Exception {
        given(friendService.sendFriendRequest(LOGIN_USER_DTO, FRIEND_REQUEST_DTO)).willReturn(true);

        mockMvc.perform(post("/friends")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(FRIEND_REQUEST_DTO))
                .sessionAttr(UserSession.LOGIN_USER, LOGIN_USER_DTO))
                .andExpect(status().isFound());

        verify(friendService, times(1)).sendFriendRequest(LOGIN_USER_DTO, FRIEND_REQUEST_DTO);
    }

    @Test
    @DisplayName("자기 자신을 친구 추가할 때 테스트")
    void friendPostFailWhenIdMatchTest() throws Exception {
        FriendRequestDto friendRequestDto = new FriendRequestDto(LOGIN_USER_DTO.getId());

        mockMvc.perform(post("/friends")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(friendRequestDto))
                .sessionAttr(UserSession.LOGIN_USER, LOGIN_USER_DTO))
                .andExpect(status().isFound());

        verify(friendService, times(0)).sendFriendRequest(LOGIN_USER_DTO, FRIEND_REQUEST_DTO);
    }
}