package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.domain.repository.CommentRepository;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest extends BaseTest {
    private static String COMMENT_CONTENTS = "comment contents";
    private static long USER_ID = 1L;
    private static long WRONG_USER_ID = 10L;
    private static long POST_ID = 2L;
    private static long COMMENT_ID = 3L;
    private static long WRONG_COMMENT_ID = 30L;

    @Mock
    private User user;

    @Mock
    private UserSession userSession;

    @Mock
    private UserSession wrongUserSession;

    @Mock
    private UserResponseDto loginUserDto;

    @Mock
    private UserResponseDto wrongLoginUserDto;

    @Mock
    private CommentRequestDto commentRequestDto;

    @Mock
    private CommentRequestDto wrongCommentRequestDto;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @InjectMocks
    CommentService commentService;

    @Mock
    Post post;

    @Mock
    Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        when(loginUserDto.getId()).thenReturn(USER_ID);
        when(userSession.getDto()).thenReturn(loginUserDto);
        when(userService.findLoggedInUser(loginUserDto)).thenReturn(user);
        when(post.getId()).thenReturn(POST_ID);
        when(postService.read(POST_ID)).thenReturn(post);

        when(comment.isMatchUser(user)).thenReturn(true);
        when(comment.getId()).thenReturn(COMMENT_ID);
        when(commentRequestDto.getCommentId()).thenReturn(COMMENT_ID);
        when(commentRequestDto.getContents()).thenReturn(COMMENT_CONTENTS);
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentRepository.findById(COMMENT_ID)).thenReturn(java.util.Optional.of(comment));

        when(wrongLoginUserDto.getId()).thenReturn(WRONG_USER_ID);
        when(wrongUserSession.getDto()).thenReturn(wrongLoginUserDto);
        when(userService.findLoggedInUser(wrongLoginUserDto)).thenThrow(new UnauthorizedException());
        when(wrongCommentRequestDto.getCommentId()).thenReturn(WRONG_COMMENT_ID);
        when(commentRepository.findById(WRONG_COMMENT_ID)).thenThrow(new CommentNotFoundException());
    }

    @Test
    @DisplayName("댓글 달기")
    void add() {
        commentService.add(commentRequestDto, userSession);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 수정")
    void update() {
        commentService.update(commentRequestDto, userSession);
        verify(comment).update(COMMENT_CONTENTS);
    }

    @Test
    @DisplayName("다른 사용자의 댓글 수정 시도")
    void update_fail_by_another_user() {
        try {
            commentService.update(commentRequestDto, wrongUserSession);
        } catch (final UnauthorizedException ignored) {}
        verify(comment, never()).update(COMMENT_CONTENTS);
    }

    @Test
    @DisplayName("Post별로 댓글 받아오기")
    void get_comments_by_post() {
        commentService.findByPost(POST_ID);
        verify(commentRepository).findByPost(any(Post.class));
    }

    @Test
    @DisplayName("댓글 삭제")
    void delete() {
        commentService.delete(commentRequestDto.getCommentId(), userSession);
        verify(commentRepository).delete(any(Comment.class));
    }

    @Test
    @DisplayName("없는 댓글 삭제 시도")
    void delete_wrong_comment_id() {
        try {
            commentService.delete(wrongCommentRequestDto.getCommentId(), userSession);
        } catch (final CommentNotFoundException ignored) {}
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    @Test
    @DisplayName("남의 댓글 삭제 시도")
    void delete_fail_by_another_user() {
        try {
            commentService.delete(commentRequestDto.getCommentId(), wrongUserSession);
        } catch (final UnauthorizedException ignored) {}
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}