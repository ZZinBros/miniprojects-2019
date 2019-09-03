package com.woowacourse.zzinbros.notification.web;

import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.dto.NotificationResponseDto;
import com.woowacourse.zzinbros.notification.service.NotificationService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationResponseDto>> fetch(Pageable pageable,
                                                               @SessionInfo UserSession userSession) {
        Page<PostNotification> postNotificationPage = notificationService
                .fetchNotifications(userSession.getDto(), pageable);
        List<PostNotification> postNotifications = postNotificationPage.getContent();
        List<NotificationResponseDto> notificationResponseDtos = postNotifications.stream()
                .map(NotificationResponseDto::new)
                .collect(toList());

        return ResponseEntity.ok(notificationResponseDtos);
    }
}
