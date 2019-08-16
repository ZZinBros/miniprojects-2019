package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.domain.repository.CommentRepository;
import com.woowacourse.zzinbros.comment.dto.CommentResponseDto;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public CommentResponseDto add(final User author, final Post post, final String contents) {
        commentRepository.save(new Comment(author, post, contents));
        return new CommentResponseDto(author.getName(), contents);
    }

    public List<Comment> findByPost(final Post post) {
        return Collections.unmodifiableList(commentRepository.findByPost(post));
    }

    public void update(final Long commentId, final String newContents, final User author) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, author);
        comment.update(newContents);
    }

    public void delete(final Long commentId, final User author) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, author);
        commentRepository.delete(comment);
    }

    private void checkMatchedUser(final Comment comment, final User user) {
        if (comment.isMatchUser(user)) {
            return;
        }
        throw new UnauthorizedException();
    }
}
