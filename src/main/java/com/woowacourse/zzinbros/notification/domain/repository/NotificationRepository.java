package com.woowacourse.zzinbros.notification.domain.repository;

import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<PostNotification, Long> {
    Page<PostNotification> findAllByNotifiedUser(User user, Pageable pageable);
}
