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
        Set<UserResponseDto> friendsDtos = friendService.findFriendsByUserId(publisher.getId());
        List<PostNotification> savedNotifications = new ArrayList<>();

        for (UserResponseDto friendDto : friendsDtos) {
            PostNotification savedNotification = notificationRepository
                    .save(new PostNotification(notificationType, publisher, friendDto.getId(), post.getId()));
            savedNotifications.add(savedNotification);
        }
        return savedNotifications;
    }

    public Page<PostNotification> fetchNotifications(long notifiedUserId, PageRequest pageRequest) {
        return notificationRepository.findAllByNotifiedUserId(notifiedUserId, pageRequest);
    }
}
