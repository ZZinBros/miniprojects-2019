package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostTest;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserTest;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostPageControllerTest extends BaseTest {

    private static final Long BASE_ID = 1L;

    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    PostService postService;

    @Autowired
    PostPageController postPageController;

    private User baseUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(postPageController)
                .alwaysDo(print())
                .build();

        baseUser = new User(UserTest.BASE_NAME, UserTest.BASE_EMAIL, UserTest.BASE_PASSWORD);
    }

    @Test
    @DisplayName("User가 존재할때 UserPage를 제대로 반환하는지")
    void showPage() throws Exception {
        List<Post> posts = Arrays.asList(
                new Post(PostTest.DEFAULT_CONTENT, baseUser),
                new Post(PostTest.DEFAULT_CONTENT, baseUser)
        );
        given(userService.findUserById(BASE_ID)).willReturn(baseUser);
        given(postService.readAllByUser(baseUser)).willReturn(posts);

        mockMvc.perform(get("/posts?author=" + BASE_ID))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("User가 존재하지 않을 때 인덱스 페이지로 리다이렉트 하는지")
    void showPageWhenUserDoesNotExist() throws Exception {
        given(userService.findUserById(BASE_ID)).willThrow(UserNotFoundException.class);

        mockMvc.perform(get("/posts?author=" + BASE_ID))
                .andExpect(status().isFound());
    }
}