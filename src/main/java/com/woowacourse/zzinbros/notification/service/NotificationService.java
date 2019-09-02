package com.woowacourse.zzinbros.notification.service;

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

import java.util.Set;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.NEW_POST;

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

    public void notifyPostCreation(Post post) {
        User publisher = post.getAuthor();
        Set<UserResponseDto> friendsDtos = friendService.findFriendsByUser(publisher.getId());

        for (UserResponseDto friendDto : friendsDtos) {
            save(new PostNotification(NEW_POST, publisher, friendDto.getId(), post.getId()));
        }
    }

    public PostNotification save(PostNotification postNotification) {
        return notificationRepository.save(postNotification);
    }

    public Page<PostNotification> fetchNotifications(long notifiedUserId, PageRequest pageRequest) {
        return notificationRepository.findAllByNotifiedUserId(notifiedUserId, pageRequest);
    }
}
