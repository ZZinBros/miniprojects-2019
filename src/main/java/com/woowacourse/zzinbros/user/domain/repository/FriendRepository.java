package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Set<Friend> findByFrom(User from);

    boolean existsByFromAndTo(User me, User other);
}
