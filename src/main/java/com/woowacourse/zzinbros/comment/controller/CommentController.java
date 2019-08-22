package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Comment> add(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession) {
        final User user = userService.findLoggedInUser(userSession.getDto());
        final Post post = postService.read(dto.getPostId());
        final String contents = dto.getContents();
        return new ResponseEntity<>(commentService.add(user, post, contents), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Comment> edit(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession) {
        final User user = userService.findLoggedInUser(userSession.getDto());
        return new ResponseEntity<>(commentService.update(dto.getCommentId(), dto.getContents(), user), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Boolean> delete(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession) {
        final User user = userService.findLoggedInUser(userSession.getDto());
        try {
            commentService.delete(dto.getCommentId(), user);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
        }
    }
}
