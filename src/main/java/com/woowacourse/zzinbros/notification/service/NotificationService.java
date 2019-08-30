package com.woowacourse.zzinbros.notification.service;

import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.domain.repository.NotificationRepository;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public PostNotification save(PostNotification postNotification) {
        return notificationRepository.save(postNotification);
    }

    public Page<PostNotification> fetchNotifications(User notifiedUser, PageRequest pageRequest) {
        return notificationRepository.findAllByNotified(notifiedUser, pageRequest);
    }
}
