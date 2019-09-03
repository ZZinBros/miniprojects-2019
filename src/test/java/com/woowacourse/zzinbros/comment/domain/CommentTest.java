package com.woowacourse.zzinbros.comment.domain;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.woowacourse.zzinbros.post.domain.DisplayType.ALL;
import static org.assertj.core.api.Assertions.assertThat;

class CommentTest extends BaseTest {

    @Test
    @DisplayName("사용자 일치 여부 확인 로직 테스트")
    void isMatchUser() {
        final User user = new User("name", "email@example.net", "password");
        final User anotherUser = new User("noname", "spam@python.org", "passphrase");
        final Post post = new Post("post", user, ALL);
        final Comment comment = new Comment(user, post, "comment");

        assertThat(comment.isAuthor(user)).isTrue();
        assertThat(comment.isAuthor(anotherUser)).isFalse();
    }
}