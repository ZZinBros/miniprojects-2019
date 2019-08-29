package com.woowacourse.zzinbros.comment.controller;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.dto.CommentResponseDto;
import com.woowacourse.zzinbros.comment.service.CommentService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable final Long postId) {
        final List<Comment> comments = commentService.findByPost(postId);
        final List<CommentResponseDto> dtoList = comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> add(
            @PathVariable final Long postId,
            @RequestBody final CommentRequestDto requestDto,
            @SessionInfo final UserSession userSession) {
        requestDto.setPostId(postId);
        final Comment comment = commentService.add(requestDto, userSession);
        final CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> edit(
            @PathVariable final Long postId,
            @PathVariable final Long commentId,
            @RequestBody final CommentRequestDto requestDto,
            @SessionInfo final UserSession session) {
        requestDto.setPostId(postId);
        requestDto.setCommentId(commentId);
        final Comment comment = commentService.update(requestDto, session);
        final CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(
            @PathVariable final Long commentId,
            @SessionInfo final UserSession session) {
        commentService.delete(commentId, session);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
