package com.woowacourse.zzinbros.notification.domain.repository;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.CREATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.by;

@DataJpaTest
class NotificationRepositoryTest extends BaseTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User notifiedUser;
    private User publisher;

    @BeforeEach
    void setUp() {
        notifiedUser = userRepository.findById(999L).orElseThrow(IllegalArgumentException::new);
        publisher = userRepository.findById(1000L).orElseThrow(IllegalArgumentException::new);
    }

    @Test
    @DisplayName("알림을 page단위로 가져온다.")
    void fetchNotificationByPage() {
        // Given
        int pageSize = 5;
        int numberOfNotifications = 15;
        PageRequest pageRequest = getPageRequest(pageSize);

        saveNotifications(numberOfNotifications);

        // When
        Page<PostNotification> notificationPage = notificationRepository
                .findAllByNotifiedUser(notifiedUser, pageRequest);
        List<PostNotification> notifications = notificationPage.getContent();

        // Then
        assertThat(notifications.size()).isEqualTo(pageSize);
    }

    @Test
    @DisplayName("알림 개수가 page 크기보다 작은 경우에 저장된 알림을 모두 가져온다.")
    void fetchNotificationByPageIfLessNumber() {
        // Given
        int pageSize = 10;
        int numberOfNotification = 5;
        PageRequest pageRequest = getPageRequest(pageSize);

        saveNotifications(numberOfNotification);

        // When
        Page<PostNotification> notificationPage = notificationRepository
                .findAllByNotifiedUser(notifiedUser, pageRequest);
        List<PostNotification> notifications = notificationPage.getContent();

        // Then
        assertThat(notifications.size()).isEqualTo(numberOfNotification);
    }

    private void saveNotifications(int numberOfNotifications) {
        for (int i = 0; i < numberOfNotifications; i++) {
            Post post = postRepository.save(new Post());
            notificationRepository.save(new PostNotification(CREATED, publisher, notifiedUser, post));
        }
    }

    private PageRequest getPageRequest(int pageSize) {
        Sort sort = by(Sort.Direction.ASC, "createdDateTime");
        return PageRequest.of(0, pageSize, sort);
    }
}