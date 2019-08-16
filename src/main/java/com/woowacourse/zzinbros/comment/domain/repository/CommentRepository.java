package com.woowacourse.zzinbros.comment.domain.repository;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(final Post post);
}
