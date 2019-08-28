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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
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
class CommentControllerTest extends BaseTest {
    private static final Long MOCK_ID = 1L;
    private static final String LOGIN_USER = "loggedInUser";
    private static final String POSTS_PATH = "/posts/";
    private static final String COMMENTS_PATH = POSTS_PATH + MOCK_ID + "/comments";
    private static final String MOCK_CONTENTS = "contents";

    private User mockUser = new User("name", "email@example.net", "12QWas!@");
    @Spy
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

    @BeforeEach
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
        given(mockPost.getId()).willReturn(MOCK_ID);
    }

    @Test
    @DisplayName("특정 Post에서 댓글 목록 가져오기 성공")
    void get_mapping_success() throws Exception {
        final List<Comment> comments = new ArrayList<>(Arrays.asList(
                new Comment(mockUser, mockPost, "comment1"),
                new Comment(mockUser, mockPost, "comment2"),
                new Comment(mockUser, mockPost, "comment3")
        ));

        given(postService.read(MOCK_ID)).willReturn(mockPost);
        given(commentService.findByPost(mockPost.getId())).willReturn(comments);

        mockMvc.perform(get(COMMENTS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].contents", is("comment1")))
                .andExpect(jsonPath("$.[1].contents", is("comment2")))
                .andExpect(jsonPath("$.[2].contents", is("comment3")));
    }

    @Test
    @DisplayName("존재하지 않는 Post에서 빈 댓글 목록 가져오기")
    void get_empty_list_by_no_post() throws Exception {
        given(postService.read(MOCK_ID)).willThrow(PostNotFoundException.class);

        mockMvc.perform(get(POSTS_PATH + 135 + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @DisplayName("특정 Post에 댓글 추가 성공")
    void add_mapping() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(postService.read(MOCK_ID)).willReturn(mockPost);
        given(commentService.add(any(), any())).willReturn(mockComment);

        mockMvc.perform(post(COMMENTS_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("지정된 댓글 편집 성공")
    void edit_mapping() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(commentService.update(any(), any())).willReturn(mockComment);

        mockMvc.perform(put(COMMENTS_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 댓글 편집 실패")
    void edit_mapping_fail_by_no_comment() throws Exception {
        given(userService.findLoggedInUser(any())).willReturn(mockUser);
        given(commentService.update(any(), any())).willThrow(new CommentNotFoundException());

        mockMvc.perform(put(COMMENTS_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void delete_mapping_success() throws Exception {
        mockMvc.perform(delete(COMMENTS_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("로그인되지 않은 사용자의 댓글 편집 거부")
    void delete_mapping_fail_by_auth() throws Exception {
        given(userService.findLoggedInUser(any())).willThrow(UserNotFoundException.class);

        mockMvc.perform(delete(POSTS_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUserDto)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is4xxClientError());
    }
}