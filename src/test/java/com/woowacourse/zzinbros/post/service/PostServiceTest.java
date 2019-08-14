package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.woowacourse.zzinbros.post.domain.PostTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;

public class PostServiceTest {
    private final Long DEFAULT_USER_ID = 999L;
    private User defaultUser;
    private Post defaultPost;
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultUser = new User(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
        defaultPost = new Post(DEFAULT_CONTENT, defaultUser);
    }

    @Test
    void 게시글_작성() {
        given(postRepository.save(defaultPost)).willReturn(defaultPost);
        PostRequestDto dto = new PostRequestDto();
        dto.setContents(DEFAULT_CONTENT);

        assertThat(postService.add(dto, defaultUser)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_수정() {
        given(postRepository.findById(DEFAULT_USER_ID)).willReturn(Optional.of(defaultPost));
        PostRequestDto dto = new PostRequestDto();
        dto.setContents(NEW_CONTENT);

        assertThat(postService.update(DEFAULT_USER_ID, dto, defaultUser)).isEqualTo(new Post(NEW_CONTENT, defaultUser));
    }

    @Test
    void 게시글_조회() {
        given(postRepository.findById(DEFAULT_USER_ID)).willReturn(Optional.of(defaultPost));
        assertThat(postService.read(DEFAULT_USER_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        given(postRepository.findById(DEFAULT_USER_ID)).willReturn(Optional.of(defaultPost));
        assertThat(postService.delete(DEFAULT_USER_ID, defaultUser)).isTrue();
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_삭제() {
        given(postRepository.findById(DEFAULT_USER_ID)).willReturn(Optional.of(defaultPost));
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> postService.delete(
                        DEFAULT_USER_ID,
                        new User("paul", "paul123@example.com", "123456789")));
    }
}
