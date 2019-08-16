package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static com.woowacourse.zzinbros.post.domain.PostTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

public class PostServiceTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long DEFAULT_POST_ID = 999L;
    private User defaultUser;
    private Post defaultPost;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultUser = new User(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
        defaultPost = new Post(DEFAULT_CONTENT, defaultUser);
        given(postRepository.findById(DEFAULT_POST_ID)).willReturn(Optional.of(defaultPost));
        given(userService.findUserById(DEFAULT_USER_ID)).willReturn(defaultUser);
    }

    @Test
    void 게시글_작성() {
        given(postRepository.save(defaultPost)).willReturn(defaultPost);
        PostRequestDto dto = new PostRequestDto();
        dto.setContents(DEFAULT_CONTENT);

        assertThat(postService.add(dto, new UserSession(DEFAULT_USER_ID, DEFAULT_NAME, DEFAULT_EMAIL))).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_수정() {
        PostRequestDto dto = new PostRequestDto();
        dto.setContents(NEW_CONTENT);

        assertThat(postService.update(DEFAULT_POST_ID, dto, new UserSession(DEFAULT_USER_ID, DEFAULT_NAME, DEFAULT_EMAIL))).isEqualTo(new Post(NEW_CONTENT, defaultUser));
    }

    @Test
    void 게시글_조회() {
        assertThat(postService.read(DEFAULT_POST_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        assertThat(postService.delete(DEFAULT_POST_ID, new UserSession(DEFAULT_USER_ID, DEFAULT_NAME, DEFAULT_EMAIL))).isTrue();
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_삭제() {
        given(userService.findUserById(1000L)).willReturn(new User("paul", "paul123@example.com", "123456789"));
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> postService.delete(
                        DEFAULT_POST_ID,
                        new UserSession(1000L, "paul", "paul123@example.com")));
    }

    @Test
    void 모든_게시글_조회() {
        given(postRepository.findAll()).willReturn(Arrays.asList(defaultPost));
        assertThat(postService.readAll()).isEqualTo(Arrays.asList(defaultPost));
    }
}
