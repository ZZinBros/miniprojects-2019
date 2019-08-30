package com.woowacourse.zzinbros.notification.domain.repository;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.*;
import static com.woowacourse.zzinbros.notification.service.NotificationServiceTest.TEST_POST_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.by;

@DataJpaTest
class NotificationRepositoryTest extends BaseTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User notifiedUser;
    private User publisher;
    private PostNotification newPostNotification;
    private PostNotification sharedPostNotification;
    private PostNotification postLikeNotification;

    @BeforeEach
    void setUp() {
        notifiedUser = userRepository.findById(999L).orElseThrow(IllegalArgumentException::new);
        publisher = userRepository.findById(1000L).orElseThrow(IllegalArgumentException::new);

        newPostNotification = new PostNotification(NEW_POST, publisher, notifiedUser, TEST_POST_ID);
        sharedPostNotification = new PostNotification(SHARED_POST, publisher, notifiedUser, TEST_POST_ID);
        postLikeNotification = new PostNotification(POST_LIKE, publisher, notifiedUser, TEST_POST_ID);
    }

    @Test
    @DisplayName("알림을 page단위로 가져온다.")
    void fetchNotificationByPage() {
        // Given
        int pageSize = 2;
        PageRequest pageRequest = getPageRequest(pageSize);

        notificationRepository.save(newPostNotification);
        notificationRepository.save(sharedPostNotification);
        notificationRepository.save(postLikeNotification);

        // When
        Page<PostNotification> notificationPage = notificationRepository.findAllByNotified(notifiedUser, pageRequest);
        List<PostNotification> notifications = notificationPage.getContent();

        // Then
        assertThat(notifications.size()).isEqualTo(pageSize);
    }

    private PageRequest getPageRequest(int pageSize) {
        Sort sort = by(Sort.Direction.ASC, "createdDateTime");
        return PageRequest.of(0,pageSize, sort);
    }

    //TODO: notification이 5개밖에 없는 경우, pageable siz가 10이더라도 5개 돌려주는지 테스트
}