package com.woowacourse.zzinbros.post.domain.repository;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostAndUser(Post post, User user);
}
