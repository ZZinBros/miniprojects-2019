package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.FriendRequest;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    void deleteBySenderAndReceiver(User sender, User receiver);

    boolean existsBySenderAndReceiver(User sender, User receiver);

    @Query("SELECT f.sender FROM FriendRequest f where f.receiver = :receiver")
    Set<User> findSendersByReceiver(@Param("receiver") User receiver);
}
