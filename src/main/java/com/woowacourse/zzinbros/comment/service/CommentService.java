package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.domain.repository.CommentRepository;
import com.woowacourse.zzinbros.comment.dto.CommentRequestDto;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentService(final UserService userService, final PostService postService, final CommentRepository commentRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment add(final CommentRequestDto requestDto, final UserSession session) {
        final User user = userService.findLoggedInUser(session.getDto());
        final Post post = postService.read(requestDto.getPostId());
        final String contents = requestDto.getContents();
        return commentRepository.save(new Comment(user, post, contents));
    }

    public List<Comment> findByPost(final long postId) {
        final Post post = postService.read(postId);
        return Collections.unmodifiableList(commentRepository.findByPost(post));
    }

    public Comment findById(final long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional
    public Comment update(final CommentRequestDto requestDto, final UserSession session) {
        final User user = userService.findLoggedInUser(session.getDto());
        final Comment comment = commentRepository
                .findById(requestDto.getCommentId())
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, user);
        comment.update(requestDto.getContents());
        return comment;
    }

    @Transactional
    public void delete(final CommentRequestDto requestDto, final UserSession session) {
        final User user = userService.findLoggedInUser(session.getDto());
        final Comment comment = commentRepository
                .findById(requestDto.getCommentId())
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, user);
        comment.prepareToDelete();
        commentRepository.delete(comment);
    }

    private void checkMatchedUser(final Comment comment, final User user) {
        if (comment.isMatchUser(user)) {
            return;
        }
        throw new UnauthorizedException();
    }
}
