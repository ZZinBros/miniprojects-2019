package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.dto.CommentResponseDto;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
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

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable final Long postId) {
        final Post post = postService.read(postId);
        final List<Comment> comments = commentService.findByPost(post);
        final List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(commentResponseDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> add(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession) throws Exception {
        final User user = userService.findLoggedInUser(userSession.getDto());
        final Post post = postService.read(dto.getPostId());
        final String contents = dto.getContents();
        final Comment comment = commentService.add(user, post, contents);
        final CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CommentResponseDto> edit(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession) throws CommentNotFoundException, UnauthorizedException {
        final User user = userService.findLoggedInUser(userSession.getDto());
        final Comment comment = commentService.update(dto.getCommentId(), dto.getContents(), user);
        final CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<CommentResponseDto> delete(@RequestBody final CommentRequestDto dto, @SessionInfo final UserSession userSession)
            throws CommentNotFoundException, UnauthorizedException {
        final User user = userService.findLoggedInUser(userSession.getDto());
        commentService.delete(dto.getCommentId(), user);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
