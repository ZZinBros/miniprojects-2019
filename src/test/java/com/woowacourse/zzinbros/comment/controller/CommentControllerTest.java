package com.woowacourse.zzinbros.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest extends BaseTest {
    private static final Long MOCK_ID = 1L;
    private static final String LOGIN_USER = "loggedInUser";
    private static final String MAPPING_PATH = "/comments";
    private static final String MAPPING_BY_POST_PATH = MAPPING_PATH + "/by-post/";
    private static final String MOCK_CONTENTS = "contents";

    private User mockUser = new User("name", "email@example.net", "12QWas!@");
    private Post mockPost = new Post(MOCK_CONTENTS, mockUser);
    private Comment mockComment = new Comment(mockUser, mockPost, MOCK_CONTENTS);
    private String commentRequestDto;
    private UserResponseDto mockUserDto = new UserResponseDto(MOCK_ID, mockUser.getName(), mockUser.getEmail());

    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    CommentController commentController;

    @MockBean
    CommentService commentService;

    @MockBean
    UserService userService;

    @MockBean
    PostService postService;

    @BeforeAll
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .build();

        final CommentRequestDto dto = new CommentRequestDto();
        dto.setPostId(MOCK_ID);
        dto.setCommentId(MOCK_ID);
        dto.setContents(MOCK_CONTENTS);
        commentRequestDto = new ObjectMapper().writeValueAsString(dto);
    }

    @Test
    void get_mapping_success() throws Exception {
        final List<Comment> comments = new ArrayList<>(Arrays.asList(
                new Comment(mockUser, mockPost, "comment1"),
                new Comment(mockUser, mockPost, "comment2"),
                new Comment(mockUser, mockPost, "comment3")
        ));

        given(postService.read(MOCK_ID)).willReturn(mockPost);
        given(commentService.findByPost(mockPost)).willReturn(comments);

        mockMvc.perform(get(MAPPING_BY_POST_PATH + MOCK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].contents", is("comment1")))
                .andExpect(jsonPath("$.[1].contents", is("comment2")))
                .andExpect(jsonPath("$.[2].contents", is("comment3")));
    }

    @Test
    void get_mapping_fail_by_no_post() throws Exception {
        given(postService.read(MOCK_ID)).willThrow(PostNotFoundException.class);

        mockMvc.perform(get(MAPPING_BY_POST_PATH + MOCK_ID))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void add_mapping() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(postService.read(MOCK_ID)).willReturn(mockPost);
        given(commentService.add(mockUser, mockPost, MOCK_CONTENTS)).willReturn(mockComment);

        mockMvc.perform(post(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    void edit_mapping() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(commentService.update(MOCK_ID, MOCK_CONTENTS, mockUser)).willReturn(mockComment);

        mockMvc.perform(put(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void edit_mapping_fail_by_no_comment() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(commentService.update(MOCK_ID, MOCK_CONTENTS, mockUser)).willThrow(CommentNotFoundException.class);

        mockMvc.perform(put(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_mapping_success() throws Exception {
        mockMvc.perform(delete(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_mapping_fail_by_auth() throws Exception {
        given(userService.findLoggedInUser(any())).willThrow(UserNotFoundException.class);

        mockMvc.perform(delete(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is5xxServerError());
    }
}