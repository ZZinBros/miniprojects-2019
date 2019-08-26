package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.post.domain.repository.PostLikeRepository;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

public class PostServiceTest extends BaseTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long DEFAULT_POST_ID = 999L;
    private User defaultUser;
    private Post defaultPost;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

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

        assertThat(postService.add(dto, DEFAULT_USER_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_수정() {
        PostRequestDto dto = new PostRequestDto();
        dto.setContents(NEW_CONTENT);

        assertThat(postService.update(DEFAULT_POST_ID, dto, DEFAULT_USER_ID)).isEqualTo(new Post(NEW_CONTENT, defaultUser));
    }

    @Test
    void 게시글_조회() {
        assertThat(postService.read(DEFAULT_POST_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        assertThat(postService.delete(DEFAULT_POST_ID, DEFAULT_USER_ID)).isTrue();
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_삭제() {
        given(userService.findUserById(1000L)).willReturn(new User("paul", "paul123@example.com", "123456789"));
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> postService.delete(
                        DEFAULT_POST_ID,
                        1000L));
    }

    @Test
    void 모든_게시글_조회() {
        given(postRepository.findAll(by(Direction.DESC, "createdDateTime"))).willReturn(Arrays.asList(defaultPost));
        assertThat(postService.readAll(by(Direction.DESC, "createdDateTime"))).isEqualTo(Arrays.asList(defaultPost));
    }

    @Test
    @DisplayName("작성자에 따른 Post 조회하는지 검증")
    void findAllByAuthor() {
        given(postRepository.findAllByAuthor(defaultUser, by(Direction.DESC, "createdDateTime"))).willReturn(Arrays.asList(defaultPost));
        assertThat(postService.readAllByUser(defaultUser, by(Direction.DESC, "createdDateTime"))).isEqualTo(Arrays.asList(defaultPost));
    }

    @Test
    void 좋아요를_누른상태에서_좋아요를_눌렀을_경우_확인() {
        PostLike postLike = new PostLike(defaultPost, defaultUser);
        given(postLikeRepository.findByPostAndUser(defaultPost, defaultUser)).willReturn(postLike);
        doNothing().when(postLikeRepository).delete(postLike);

        assertThat(postService.updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID)).isEqualTo(INIT_LIKE);
    }

    @Test
    void 좋아요를_누르지_않은_상태에서_좋아요를_눌렀을_경우_확인() {
        given(postLikeRepository.findByPostAndUser(defaultPost, defaultUser)).willReturn(null);
        assertThat(postService.updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID)).isEqualTo(INIT_LIKE + 1);
    }
}
