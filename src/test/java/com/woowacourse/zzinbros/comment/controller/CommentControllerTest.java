package com.woowacourse.zzinbros.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
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

import javax.servlet.http.HttpSession;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class CommentControllerTest {
    private static final Long MOCK_ID = 1L;
    private static final String LOGIN_USER = "loggedInUser";
    private static final String MAPPING_PATH = "/comments";
    private static final String MOCK_CONTENTS = "contents";

    private User mockUser = new User("name", "email@example.net", "12QWas!@");
    private Post mockPost = new Post(MOCK_CONTENTS, mockUser);
    private Comment mockComment = new Comment(mockUser, mockPost, MOCK_CONTENTS);
    private String commentRequestDto;

    private MockMvc mockMvc;

    @Autowired
    CommentController commentController;

    @MockBean
    CommentService commentService;

    @MockBean
    HttpSession httpSession;

    @MockBean
    UserService userService;

    @MockBean
    PostService postService;

    @BeforeAll
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(commentController)
                .alwaysDo(print())
                .build();

        final CommentRequestDto dto = new CommentRequestDto();
        dto.setPostId(MOCK_ID);
        dto.setCommentId(MOCK_ID);
        dto.setContents(MOCK_CONTENTS);
        commentRequestDto = new ObjectMapper().writeValueAsString(dto);
    }

    @Test
    void add_mapping() throws Exception {
        given(postService.read(MOCK_ID)).willReturn(mockPost);

        mockMvc.perform(post(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUser)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void edit_mapping() throws Exception {
        given(commentService.update(MOCK_ID, MOCK_CONTENTS, mockUser)).willReturn(mockComment);

        mockMvc.perform(put(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUser)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void delete_mapping() throws Exception {
        mockMvc.perform(delete(MAPPING_PATH)
                .content(commentRequestDto)
                .sessionAttr(LOGIN_USER, mockUser)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("true"));
    }
}