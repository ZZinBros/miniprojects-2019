package com.woowacourse.zzinbros.notification.service;

import com.woowacourse.zzinbros.notification.domain.NotificationType;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.domain.repository.NotificationRepository;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class NotificationService {
    private NotificationRepository notificationRepository;
    private FriendService friendService;

    public NotificationService(
            NotificationRepository notificationRepository,
            FriendService friendService) {
        this.notificationRepository = notificationRepository;
        this.friendService = friendService;
    }

    public List<PostNotification> notify(Post post, NotificationType notificationType) {
        User publisher = post.getAuthor();
        Set<User> friends = friendService.findFriendEntitiesByUser(publisher.getId());
        List<PostNotification> notifications = new ArrayList<>();

        for (User notifiedUser : friends) {
            notifications.add(new PostNotification(notificationType, publisher, notifiedUser, post));
        }
        notificationRepository.saveAll(notifications);
        return notifications;
    }

    public Page<PostNotification> fetchNotifications(User notifiedUser, PageRequest pageRequest) {
        return notificationRepository.findAllByNotifiedUser(notifiedUser, pageRequest);
    }
}
