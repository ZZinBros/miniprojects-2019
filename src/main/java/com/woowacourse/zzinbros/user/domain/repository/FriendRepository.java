package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.TempUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Set<Friend> findByFrom(TempUser from);
}
