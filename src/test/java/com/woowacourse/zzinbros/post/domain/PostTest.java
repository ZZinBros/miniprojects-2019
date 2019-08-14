package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PostTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john", "john123@example.com", "123456789");
    }

    @Test
    void 생성자_테스트() {
        assertDoesNotThrow(() -> new Post("some contents", user));
    }

    @Test
    void 게시글_작성자가_게시글_수정_테스트() {
        Post oldPost = new Post("oldPost", user);
        Post newPost = new Post("newPost", user);
        assertThat((oldPost.update(newPost)).getContents()).isEqualTo("newPost");
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_수정_테스트() {
        Post oldPost = new Post("oldPost", user);
        Post newPost = new Post("newPost", new User("paul", "paul123@example.com", "123456789"));
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> oldPost.update(newPost));

        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> newPost.update(oldPost));

    }
}
