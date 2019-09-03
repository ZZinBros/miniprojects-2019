package com.woowacourse.zzinbros.comment.web.controller.rest;

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

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.findByPost(postId);
        List<CommentResponseDto> dtoList = comments.stream()
                .map(CommentResponseDto::new)
                .collect(toList());
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> add(@PathVariable Long postId,
                                                  @RequestBody CommentRequestDto requestDto,
                                                  @SessionInfo UserSession userSession) {
        requestDto.setPostId(postId);
        Comment comment = commentService.add(requestDto, userSession.getDto());
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> edit(@PathVariable Long commentId,
                                                   @RequestBody CommentRequestDto requestDto,
                                                   @SessionInfo UserSession session) {
        requestDto.setCommentId(commentId);
        Comment comment = commentService.update(requestDto, session.getDto());
        CommentResponseDto responseDto = new CommentResponseDto(comment);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> delete(@PathVariable Long commentId,
                                                     @SessionInfo UserSession session) {
        commentService.delete(commentId, session.getDto());
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
