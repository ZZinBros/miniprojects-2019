package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceTest extends BaseTest {
    private static String USER_NAME = "name";
    private static String USER_EMAIL = "user@mail.com";
    private static String USER_PASSWORD = "asdfQWER1234!@#$";
    private static String POST_CONTENTS = "post contents";
    private static String COMMENT_CONTENTS = "comment contents";
    private static String NEW_CONTENTS = "new contents";

    private User user;
    private User wrongUser;
    private Post post;
    private Comment comment;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @BeforeAll
    void setUp() {
        user = userService.register(new UserRequestDto(USER_NAME, USER_EMAIL, USER_PASSWORD));
        wrongUser = userService.register(new UserRequestDto("wrong", "not@mail.net", "123456789"));
        final PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContents(POST_CONTENTS);
        post = postService.add(postRequestDto, user.getId());
        comment = commentService.add(user, post, COMMENT_CONTENTS);
    }

    @Test
    void 댓글_달기() {
        comment = commentService.add(user, post, COMMENT_CONTENTS);
        assertThat(comment.getAuthor()).isEqualTo(user);
        assertThat(comment.getPost()).isEqualTo(post);
        assertThat(comment.getContents()).isEqualTo(COMMENT_CONTENTS);
    }

    @Test
    void 댓글_수정() {
        comment = commentService.update(comment.getId(), NEW_CONTENTS, user);
        assertThat(comment.getContents()).isEqualTo(NEW_CONTENTS);
    }

    @Test
    void 댓글_다른_사용자_수정시도() {
        assertThatThrownBy(() -> commentService.update(comment.getId(), NEW_CONTENTS, wrongUser))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void 댓글_목록_Post별로() {
        final List<Comment> commentList = commentService.findByPost(post);
        assertThat(commentList.isEmpty()).isFalse();
    }

    @Test
    void 댓글_삭제() {
        final Comment newComment = commentService.add(user, post, NEW_CONTENTS);
        commentService.delete(newComment.getId(), user);
        assertThatThrownBy(() -> commentService.findById(newComment.getId()))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void 없는_댓글_삭제시도() {
        assertThatThrownBy(() -> commentService.delete(comment.getId() + 123L, user))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void 댓글_다른_사용자_삭제시도() {
        assertThatThrownBy(() -> commentService.delete(comment.getId(), wrongUser))
                .isInstanceOf(UnauthorizedException.class);
    }
}