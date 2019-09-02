package com.woowacourse.zzinbros.notification.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.common.domain.TestBaseMock;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.domain.repository.NotificationRepository;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.NEW_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.by;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTest extends BaseTest {
    public static final long TEST_POST_ID = 999L;
    private static final long PUBLISHER_ID = 999L;
    private static final long NOTIFIED_USER_ID = 1000L;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private NotificationService notificationService;

    private User publisher;
    private User notifiedUser;

    @BeforeEach
    void setUp() {
        publisher = new User("publisher", "publisher@test.com", "!@QW12qw");
        notifiedUser = new User("testUser", "test@test.com", "!@QW12qw");
    }

    @Test
    @DisplayName("특정 User에게 알려줄 알림을 저장한다.")
    void savePostNotification() {
        // Given
        PostNotification postNotification = new PostNotification(
                NEW_POST, publisher, NOTIFIED_USER_ID, TEST_POST_ID);
        given(notificationRepository.save(postNotification)).willReturn(postNotification);

        // When
        PostNotification savedNotification = notificationService.save(postNotification);

        // Then
        assertThat(savedNotification).isEqualTo(postNotification);
    }

    @Test
    @DisplayName("새로 생성된 게시글을 넘겨받아 해당 게시글 생성자의 친구들 모두에게 알림을 보낸다.")
    void saveNotificationsOfCreatedPost() {
        // Given
        int numberOfFriends = 5;
        User publisherWithId = TestBaseMock.mockingId(publisher, PUBLISHER_ID);
        Post createdPost = new Post("contents", publisherWithId);
        Set<UserResponseDto> friendsOfPublisher = getFriendsDtos(numberOfFriends);

        given(friendService.findFriendsByUser(publisherWithId.getId())).willReturn(friendsOfPublisher);

        // When
        notificationService.notifyPostCreation(createdPost);

        // Then
        verify(notificationRepository, times(numberOfFriends)).save(any());
    }

    @Test
    @DisplayName("특정 User에게 알려줄 알림을 page 단위로 조회한다.")
    void fetchNotificationsPage() {
        // Given
        int pageSize = 10;

        PageRequest pageRequest = getPageRequest(pageSize);
        Page<PostNotification> expectedPage = getPostNotifications(pageSize);
        given(notificationRepository.findAllByNotifiedUserId(NOTIFIED_USER_ID, pageRequest))
                .willReturn(expectedPage);

        // When
        Page<PostNotification> actualPage = notificationService.fetchNotifications(
                NOTIFIED_USER_ID, pageRequest);

        // Then
        assertThat(actualPage.getContent().size()).isEqualTo(pageSize);
        assertThat(actualPage).isEqualTo(expectedPage);
    }


    @Test
    @DisplayName("알림을 삭제한다")
    void delete() {
        //TODO
    }

    @Test
    @DisplayName("알림 대상이 아닌 경우 알림을 조회할 수 없다.")
    void fetchFailIfNotOwner() {
        //TODO
    }

    @Test
    @DisplayName("알림 대상이 아닌 경우 알림을 삭제할 수 없다.")
    void deleteFailIfNotOwner() {
        //TODO
    }

    private Set<UserResponseDto> getFriendsDtos(int numberOfFriends) {
        Set<UserResponseDto> userResponseDtos = new HashSet<>();

        for (int i = 0; i < numberOfFriends; i++) {
            userResponseDtos.add(new UserResponseDto(
                    NOTIFIED_USER_ID + i,
                    notifiedUser.getName(),
                    notifiedUser.getEmail())
            );
        }

        return userResponseDtos;
    }


    private PageRequest getPageRequest(int pageSize) {
        Sort sort = by(Sort.Direction.ASC, "createdDateTime");
        return PageRequest.of(0, pageSize, sort);
    }

    private Page<PostNotification> getPostNotifications(int pageSize) {
        List<PostNotification> postNotifications = new ArrayList<>();

        for (int i = 0; i < pageSize; i++) {
            postNotifications.add(new PostNotification(
                    NEW_POST, publisher, NOTIFIED_USER_ID, TEST_POST_ID + i
            ));
        }
        return new PageImpl<>(postNotifications);
    }
}