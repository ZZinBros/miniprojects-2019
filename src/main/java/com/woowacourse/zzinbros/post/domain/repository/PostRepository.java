package com.woowacourse.zzinbros.post.domain.repository;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthor(User user, Sort sort);
}
