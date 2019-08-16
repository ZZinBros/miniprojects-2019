package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    public CommentController(final CommentService commentService, final PostService postService, final UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    // TODO: 차후 UserService에서 현재 로그인된 유저를 가져오는 기능을 제공하면 삭제
    private User getLoggedInUser(final HttpSession session) {
        final User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            throw new UnauthorizedException();
        }
        return user;
    }

    @PostMapping
    public Comment add(@RequestBody final CommentRequestDto dto, final HttpSession session) {
        final User user = getLoggedInUser(session);
        final Post post = postService.read(dto.getPostId());
        final String contents = dto.getContents();
        return commentService.add(user, post, contents);
    }
}
