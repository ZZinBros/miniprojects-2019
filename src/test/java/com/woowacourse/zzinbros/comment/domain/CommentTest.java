package com.woowacourse.zzinbros.comment.domain;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest extends BaseTest {

    @Test
    void isMatchUser() {
        final User user = new User("name", "email@example.net", "password");
        final User anotherUser = new User("noname", "spam@python.org", "passphrase");
        final Post post = new Post("post", user);
        final Comment comment = new Comment(user, post, "comment");

        assertThat(comment.isMatchUser(user)).isTrue();
        assertThat(comment.isMatchUser(anotherUser)).isFalse();
    }
}