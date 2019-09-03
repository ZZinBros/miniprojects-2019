package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    boolean existsByOwnerAndSlave(User owner, User slave);

    void deleteByOwnerAndSlave(User owner, User friend);

    @Query("SELECT f.slave FROM Friend f WHERE f.owner = :owner")
    Set<User> findSlavesByOwner(@Param("owner") User owner);
}
