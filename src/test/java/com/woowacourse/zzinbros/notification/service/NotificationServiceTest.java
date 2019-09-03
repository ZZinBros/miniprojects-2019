package com.woowacourse.zzinbros.notification.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.common.domain.TestBaseMock;
import com.woowacourse.zzinbros.notification.domain.PostNotification;
import com.woowacourse.zzinbros.notification.domain.repository.NotificationRepository;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.service.UserService;
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

import static com.woowacourse.zzinbros.notification.domain.NotificationType.CREATED;
import static com.woowacourse.zzinbros.post.domain.DisplayType.ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.by;

@ExtendWith(SpringExtension.class)
public class NotificationServiceTest extends BaseTest {
    private static final long PUBLISHER_ID = 999L;
    private static final long NOTIFIED_ID = 1000L;
    private static final String PUBLISHER_NAME = "publisher";
    private static final String NOTIFIED_USER_NAME = "notified";
    private static final String PUBLISHER_EMAIL = "publisher@test.com";
    private static final String NOTIFIED_EMAIL = "notified@test.com";
    private static final String PASSWORD = "!@QW12qw";

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private FriendService friendService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationService notificationService;

    private User publisher;
    private User notifiedUser;

    @BeforeEach
    void setUp() {
        publisher = new User(PUBLISHER_NAME, PUBLISHER_EMAIL, PASSWORD);
        notifiedUser = new User(NOTIFIED_USER_NAME, NOTIFIED_EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("게시글과 알림 타입을 넘겨받아 해당 게시글 생성자의 친구들 모두에게 알림을 보낸다.")
    void saveNotificationsOfCreatedPost() {
        // Given
        int numberOfFriends = 5;
        User publisherWithId = TestBaseMock.mockingId(publisher, PUBLISHER_ID);
        Post post = new Post("contents", publisherWithId, ALL);
        Set<User> friendsOfPublisher = getFriends(numberOfFriends);

        given(friendService.findFriendEntitiesByUser(publisherWithId.getId()))
                .willReturn(friendsOfPublisher);

        // When
        List<PostNotification> savedNotifications = notificationService.notify(post, CREATED);

        // Then
        assertThat(savedNotifications.size()).isEqualTo(numberOfFriends);
    }

    @Test
    @DisplayName("친구가 한 명도 없는 경우에는 알림이 생기지 않는다.")
    void notSaveNotificationIfHasNoFriends() {
        // Given
        int numberOfFriends = 0;
        User publisherWithId = TestBaseMock.mockingId(publisher, PUBLISHER_ID);
        Post post = new Post("contents", publisherWithId, ALL);
        Set<User> friendsOfPublisher = getFriends(numberOfFriends);

        given(friendService.findFriendEntitiesByUser(publisherWithId.getId())).willReturn(friendsOfPublisher);

        // When
        List<PostNotification> savedNotifications = notificationService.notify(post, CREATED);

        // Then
        assertThat(savedNotifications.size()).isEqualTo(numberOfFriends);
    }

    @Test
    @DisplayName("특정 User에게 알려줄 알림을 page 단위로 조회한다.")
    void fetchNotificationsPage() {
        // Given
        int pageSize = 10;

        PageRequest pageRequest = getPageRequest(pageSize);
        UserResponseDto loggedInUserDto = new UserResponseDto(NOTIFIED_ID, NOTIFIED_USER_NAME, NOTIFIED_EMAIL);
        Page<PostNotification> expectedPage = getPostNotifications(pageSize);
        given(notificationRepository.findAllByNotifiedUser(notifiedUser, pageRequest))
                .willReturn(expectedPage);
        given(userService.findLoggedInUser(loggedInUserDto)).willReturn(notifiedUser);

        // When
        Page<PostNotification> actualPage = notificationService.fetchNotifications(loggedInUserDto, pageRequest);

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

    private Set<User> getFriends(int numberOfFriends) {
        Set<User> friends = new HashSet<>();
        User friend;

        for (int i = 0; i < numberOfFriends; i++) {
            friend = new User(NOTIFIED_USER_NAME + i, NOTIFIED_EMAIL + i, PASSWORD);
            friends.add(TestBaseMock.mockingId(friend, i));
        }
        return friends;
    }

    private PageRequest getPageRequest(int pageSize) {
        Sort sort = by(Sort.Direction.ASC, "createdDateTime");
        return PageRequest.of(0, pageSize, sort);
    }

    private Page<PostNotification> getPostNotifications(int pageSize) {
        List<PostNotification> postNotifications = new ArrayList<>();

        for (int i = 0; i < pageSize; i++) {
            postNotifications.add(new PostNotification(
                    CREATED, publisher, notifiedUser, new Post()
            ));
        }
        return new PageImpl<>(postNotifications);
    }
}