package com.woowacourse.zzinbros.post.domain.repository;

import com.woowacourse.zzinbros.post.domain.SharedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SharedPostRepository extends JpaRepository<SharedPost, Long> {
}
