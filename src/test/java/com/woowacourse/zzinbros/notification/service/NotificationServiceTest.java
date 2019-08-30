package com.woowacourse.zzinbros.notification.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.domain.repository.NotificationRepository;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.handler.NotifiedMethodInvocationReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.NEW_POST;
import static com.woowacourse.zzinbros.notification.domain.NotificationType.POST_LIKE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.by;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTest extends BaseTest {
    public static final long TEST_POST_ID = 999L;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    private User notifiedUser;
    private User publisher;
    private PostNotification postNotification;

    @BeforeEach
    void setUp() {
        notifiedUser = new User("testUser", "test@test.com", "!@QW12qw");
        publisher = new User("publisher", "publisher@test.com", "!@QW12qw");
        postNotification = new PostNotification(NEW_POST, publisher, notifiedUser, TEST_POST_ID);
    }

    @Test
    @DisplayName("특정 User에게 알려줄 알림을 저장한다")
    void save() {
        given(notificationRepository.save(postNotification)).willReturn(postNotification);

        PostNotification savedNotification = notificationService.save(postNotification);

        assertThat(savedNotification.getType()).isEqualTo(NEW_POST);
        assertThat(savedNotification.getPublisher().getEmail()).isEqualTo(publisher.getEmail());
        assertThat(savedNotification.getNotified().getEmail()).isEqualTo(notifiedUser.getEmail());
        assertThat(savedNotification.getPostId()).isEqualTo(TEST_POST_ID);
    }

    @Test
    @DisplayName("특정 User에게 알려줄 알림을 page 단위로 조회한다.")
    void fetch() {
        // Given
        int pageSize = 10;

        PageRequest pageRequest = getPageRequest(pageSize);
        Page<PostNotification> expectedPage = getPostNotifications(pageSize);
        given(notificationRepository.findAllByNotified(notifiedUser, pageRequest)).willReturn(expectedPage);

        // When
        Page<PostNotification> actualPage = notificationService.fetchNotifications(notifiedUser, pageRequest);

        // Then
        assertThat(actualPage.getContent().size()).isEqualTo(pageSize);
        assertThat(actualPage).isEqualTo(expectedPage);
    }

    private PageRequest getPageRequest(int pageSize) {
        Sort sort = by(Sort.Direction.ASC, "createdDateTime");
        return PageRequest.of(0, pageSize, sort);
    }

    private Page<PostNotification> getPostNotifications(int pageSize) {
        List<PostNotification> postNotifications = new ArrayList<>();

        for (int i = 0; i < pageSize; i++) {
            postNotifications.add(new PostNotification(NEW_POST, publisher, notifiedUser, TEST_POST_ID + i));
        }
        return new PageImpl<>(postNotifications);
    }

    @Test
    @DisplayName("알림을 삭제한다")
    void delete() {
    }

    @Test
    @DisplayName("알림 대상이 아닌 경우 알림을 조회할 수 없다.")
    void fetchFailIfNotOwner() {

    }

    @Test
    @DisplayName("알림 대상이 아닌 경우 알림을 삭제할 수 없다.")
    void deleteFailIfNotOwner() {
    }
}