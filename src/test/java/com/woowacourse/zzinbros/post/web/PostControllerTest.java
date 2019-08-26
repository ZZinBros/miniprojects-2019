package com.woowacourse.zzinbros.post.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.post.web.support.PostControllerExceptionAdvice;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PostControllerTest extends BaseTest {
    private static final long DEFAULT_USER_ID = 999L;
    private static final long DEFAULT_POST_ID = 999L;
    private static final String POST_URL = "/posts/";
    @InjectMocks
    private PostController postController;
    @Mock
    private PostService postService;
    private Post defaultPost;
    private User defaultUser;
    private UserResponseDto loginUserDto;
    private PostRequestDto postRequestDto;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setControllerAdvice(new PostControllerExceptionAdvice())
                .setCustomArgumentResolvers(new UserArgumentResolver())
                .alwaysDo(print())
                .build();

        loginUserDto = new UserResponseDto(DEFAULT_USER_ID, "john", "john123@example.com");
        defaultUser = new User("john", "john123@example.com", "p@ssW0rd");
        defaultPost = new Post("content", defaultUser);
        postRequestDto = new PostRequestDto();
        postRequestDto.setContents("content");
    }

    @Test
    void 게시글_생성() throws Exception {
        given(postService.add(postRequestDto, DEFAULT_USER_ID)).willReturn(defaultPost);

        mockMvc.perform(post(POST_URL)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto)
                .content(new ObjectMapper().writeValueAsString(postRequestDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());

        verify(postService, times(1)).add(postRequestDto, DEFAULT_USER_ID);
    }

    @Test
    void 게시글_수정() throws Exception {
        Post updatePost = new Post("new content", defaultUser);
        PostRequestDto updatePostDto = new PostRequestDto();
        updatePostDto.setContents("new content");
        given(postService.update(DEFAULT_POST_ID, postRequestDto, DEFAULT_USER_ID)).willReturn(updatePost);

        mockMvc.perform(put(POST_URL + DEFAULT_POST_ID)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto)
                .content(new ObjectMapper().writeValueAsString(updatePostDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted());

        verify(postService, times(1)).update(DEFAULT_POST_ID, updatePostDto, DEFAULT_USER_ID);
    }

    @Test
    void 게시글_삭제() throws Exception {
        given(postService.delete(DEFAULT_POST_ID, DEFAULT_USER_ID)).willReturn(true);

        mockMvc.perform(delete(POST_URL + DEFAULT_POST_ID)
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().isNoContent());

        verify(postService, times(1)).delete(DEFAULT_POST_ID, DEFAULT_USER_ID);
    }

    @Test
    void 게시글_조회() throws Exception {
        given(postService.read(DEFAULT_POST_ID)).willReturn(defaultPost);

        mockMvc.perform(get(POST_URL + DEFAULT_POST_ID))
                .andExpect(status().isOk());

        verify(postService, times(1)).read(DEFAULT_POST_ID);
    }

    @Test
    void 게시글_좋아요() throws Exception {
        given(postService.updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID)).willReturn(1);

        mockMvc.perform(put(POST_URL + DEFAULT_POST_ID + "/like")
                .sessionAttr(UserSession.LOGIN_USER, loginUserDto))
                .andExpect(status().isOk());

        verify(postService, times(1)).updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID);
    }
}
