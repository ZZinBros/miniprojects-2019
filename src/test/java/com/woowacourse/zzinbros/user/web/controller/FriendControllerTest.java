package com.woowacourse.zzinbros.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserBaseTest;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    @DisplayName("로그인 한 유저한테 온 친구 요청을 조회")
    void friendRequestsGet() throws Exception {
        User me = userSampleOf(SAMPLE_ONE);
        User first = userSampleOf(SAMPLE_TWO);
        User second = userSampleOf(SAMPLE_THREE);

        Friend friend2 = mockingId(Friend.of(second, me), 2L);
        Friend friend3 = mockingId(Friend.of(first, me), 3L);

        Set<Friend> friends = new HashSet<>(Arrays.asList(
                friend2,
                friend3
        ));

        Set<UserResponseDto> expected = new HashSet<>(Arrays.asList(
                new UserResponseDto(second.getId(), second.getName(), second.getEmail()),
                new UserResponseDto(first.getId(), first.getName(), first.getEmail())
        ));

        given(friendService.findFriendRequestsByUser(LOGIN_USER_DTO)).willReturn(expected);

        MvcResult mvcResult = mockMvc.perform(get("/friends/requests")
                .sessionAttr(UserSession.LOGIN_USER, LOGIN_USER_DTO))
                .andExpect(status().isOk())
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        assertEquals(new ObjectMapper().writeValueAsString(expected), actual);
        verify(friendService, times(1)).findFriendRequestsByUser(LOGIN_USER_DTO);
    }
}