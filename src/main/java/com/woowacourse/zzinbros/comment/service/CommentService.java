package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.domain.repository.CommentRepository;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Comment add(final User author, final Post post, final String contents) {
        return commentRepository.save(new Comment(author, post, contents));
    }

    public List<Comment> findByPost(final Post post) {
        return Collections.unmodifiableList(commentRepository.findByPost(post));
    }

    public Comment findById(final long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Transactional
    public Comment update(final long commentId, final String newContents, final User author) {
        final Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, author);
        comment.update(newContents);
        return comment;
    }

    @Transactional
    public void delete(final long commentId, final User author) {
        final Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        checkMatchedUser(comment, author);
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
