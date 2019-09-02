package com.woowacourse.zzinbros.comment.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class CommentService {
    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    public CommentService(UserService userService, PostService postService, CommentRepository commentRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    public Comment add(CommentRequestDto requestDto, UserResponseDto loginDto) {
        User user = userService.findLoggedInUser(loginDto);
        Post post = postService.read(requestDto.getPostId());
        String contents = requestDto.getContents();
        return commentRepository.save(new Comment(user, post, contents));
    }

    public List<Comment> findByPost(long postId) {
        Post post = postService.read(postId);
        return Collections.unmodifiableList(commentRepository.findByPost(post));
    }

    public Comment update(CommentRequestDto requestDto, UserResponseDto loginDto) {
        User user = userService.findLoggedInUser(loginDto);
        Comment comment = commentRepository
                .findById(requestDto.getCommentId())
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, user);
        comment.update(requestDto.getContents());
        return comment;
    }

    public boolean delete(long commentId, UserResponseDto loginDto) {
        User user = userService.findLoggedInUser(loginDto);
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, user);
        comment.prepareToDelete();
        commentRepository.delete(comment);
        return true;
    }

    private void checkMatchedUser(Comment comment, User author) {
        if (comment.isAuthor(author)) {
            return;
        }
        throw new UnauthorizedException();
    }
}
