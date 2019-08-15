package com.woowacourse.zzinbros.comment.service;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.comment.exception.CommentNotFoundException;
import com.woowacourse.zzinbros.comment.exception.UnauthorizedException;
import com.woowacourse.zzinbros.comment.repository.CommentRepository;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(final CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment add(final User author, final Post post, final String contents) {
        return commentRepository.save(new Comment(author, post, contents));
    }

    public List<Comment> findByPost(final Post post) {
        return commentRepository.findByPost(post);
    }

    public Comment update(final Long commentId, final String newContents, final User author) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        return comment.update(newContents, author);
    }

    public void delete(final Long commentId, final User author) {
        final Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        if (comment.isMatchUser(author)) {
            commentRepository.delete(comment);
            return;
        }
        throw new UnauthorizedException();
    }
}
