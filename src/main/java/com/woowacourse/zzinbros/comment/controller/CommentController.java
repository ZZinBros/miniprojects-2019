package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;

// 댓글을 어떻게 서버에서 보내줘야 할까요?
// 무조건 Post와 같이? 아니면 별도로 AJAX JSON으로?
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    public CommentController(final CommentService commentService, final UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }
}
