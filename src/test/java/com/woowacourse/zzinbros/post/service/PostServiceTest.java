package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.notification.service.NotificationService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.post.domain.SharedPost;
import com.woowacourse.zzinbros.post.domain.repository.PostLikeRepository;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.domain.repository.SharedPostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingIdAndCreatedDateTime;
import static com.woowacourse.zzinbros.post.domain.DisplayType.*;
import static com.woowacourse.zzinbros.post.domain.PostTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

class PostServiceTest extends BaseTest {
    private static final Long DEFAULT_USER_ID = 999L;
    private static final Long FRIEND_USER_ID = 1000L;
    private static final Long OUTSIDER_USER_ID = 1001L;
    private static final Long DEFAULT_POST_ID = 999L;

    private User defaultUser;
    private User defaultFriend;
    private User outsider;

    private Post defaultPost;

    private Post defaultPostToAll;
    private Post defaultPostToFriend;
    private Post defaultPostToMe;

    private Post friendPostToAll;
    private Post friendPostToFriend;

    private Post outsiderPostToAll;


    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @Mock
    private SharedPostRepository sharedPostRepository;

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private FriendService friendService;

    @InjectMocks
    private PostService postService;

    private PostRequestDto postRequestDto;

    private Sort sort = by(Direction.DESC, "createdDateTime");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        defaultUser = mockingId(new User(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD), DEFAULT_USER_ID);
        defaultFriend = mockingId(new User("friend", "friend@gmail.com", "12345678"), FRIEND_USER_ID);
        outsider = mockingId(new User("outsider", "outsider@gmail.com", "12345678"), OUTSIDER_USER_ID);

        defaultPost = mockingId(new Post(DEFAULT_CONTENT, defaultUser, ALL), DEFAULT_POST_ID);

        defaultPostToAll = mockingIdAndCreatedDateTime(
                new Post("defaultToAll", defaultUser, ALL),
                DEFAULT_POST_ID,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 0), ZoneOffset.of("+09:00")));
        defaultPostToFriend = mockingIdAndCreatedDateTime(
                new Post("defaultToFriend", defaultUser, FRIEND),
                DEFAULT_POST_ID + 1,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 1), ZoneOffset.of("+09:00")));
        defaultPostToMe = mockingIdAndCreatedDateTime(
                new Post("defaultToMe", defaultUser, ONLY_ME),
                DEFAULT_POST_ID + 2,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 2), ZoneOffset.of("+09:00")));

        friendPostToAll = mockingIdAndCreatedDateTime(
                new Post("friendToAll", defaultFriend, ALL),
                DEFAULT_POST_ID + 3,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 3), ZoneOffset.of("+09:00")));
        friendPostToFriend = mockingIdAndCreatedDateTime(
                new Post("friendToFriend", defaultFriend, FRIEND),
                DEFAULT_POST_ID + 4,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 4), ZoneOffset.of("+09:00")));

        outsiderPostToAll = mockingIdAndCreatedDateTime(
                new Post("outsiderToAll", outsider, ALL),
                DEFAULT_POST_ID + 5,
                OffsetDateTime.of(LocalDateTime.of(2019, 8, 30, 12, 0, 5), ZoneOffset.of("+09:00")));

        given(postRepository.findById(DEFAULT_POST_ID)).willReturn(Optional.of(defaultPost));
        given(userService.findUserById(DEFAULT_USER_ID)).willReturn(defaultUser);
        given(postRepository.findAllByDisplayTypeAndAuthor(ALL, defaultUser, sort)).willReturn(Arrays.asList());

        postRequestDto = new PostRequestDto();
        postRequestDto.setContents(DEFAULT_CONTENT);
    }

    @Test
    void 게시글_작성() {
        given(postRepository.save(new Post(DEFAULT_CONTENT, defaultUser, ALL))).willReturn(defaultPost);
        assertThat(postService.add(postRequestDto, DEFAULT_USER_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_수정() {
        postRequestDto.setContents(NEW_CONTENT);
        assertThat(postService.update(DEFAULT_POST_ID, postRequestDto, DEFAULT_USER_ID)).isEqualTo(
                mockingId(new Post(NEW_CONTENT, defaultUser, ALL), DEFAULT_POST_ID));
    }

    @Test
    void 게시글_조회() {
        assertThat(postService.read(DEFAULT_POST_ID)).isEqualTo(defaultPost);
    }

    @Test
    void 게시글_작성자가_게시글_삭제() {
        assertThat(postService.delete(DEFAULT_POST_ID, DEFAULT_USER_ID)).isTrue();
    }

    @Test
    void 게시글_작성자가_아닌_회원이_게시글_삭제() {
        given(userService.findUserById(1000L)).willReturn(new User("paul", "paul123@example.com", "123456789"));
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> postService.delete(
                        DEFAULT_POST_ID,
                        1000L));
    }

    @Test
    @DisplayName("친구관계가 아닌 회원이 회원페이지에 접속했을 때 게시글 조회")
    void readAllByOutsider() {
        given(postRepository.findAllByDisplayTypeAndAuthor(ALL, outsider, sort)).willReturn(Arrays.asList(outsiderPostToAll));
        given(friendService.isMyFriend(DEFAULT_USER_ID, FRIEND_USER_ID)).willReturn(false);
        assertThat(postService.readAllByUser(outsider, DEFAULT_USER_ID, sort)).isEqualTo(Arrays.asList(outsiderPostToAll));
    }

    @Test
    @DisplayName("친구관계인 회원이 회원페이지에 접속했을 때 게시글 조회")
    void readAllByFriend() {
        given(postRepository.findAllByDisplayTypeAndAuthor(ALL, defaultFriend, sort)).willReturn(Arrays.asList(friendPostToAll));
        given(postRepository.findAllByDisplayTypeAndAuthor(FRIEND, defaultFriend, sort)).willReturn(Arrays.asList(friendPostToFriend));
        given(friendService.isMyFriend(DEFAULT_USER_ID, FRIEND_USER_ID)).willReturn(true);
        assertThat(postService.readAllByUser(defaultFriend, DEFAULT_USER_ID, sort)).isEqualTo(Arrays.asList(friendPostToFriend, friendPostToAll));
    }

    @Test
    @DisplayName("로그인한 회원이 자신의 회원페이지에 접속했을 때 게시글 조회")
    void readAllByMe() {
        given(postRepository.findAllByAuthor(defaultUser, sort)).willReturn(Arrays.asList(defaultPostToMe, defaultPostToFriend, defaultPostToAll));
        assertThat(postService.readAllByUser(defaultUser, DEFAULT_USER_ID, sort)).isEqualTo(Arrays.asList(defaultPostToMe, defaultPostToFriend, defaultPostToAll));
    }

    @Test
    @DisplayName("타임라인에 나오는 게시글 조회")
    void readAll() {
        given(friendService.findFriendEntitiesByUser(DEFAULT_USER_ID)).willReturn(new HashSet<>(Arrays.asList(defaultFriend)));
        given(postRepository.findAllByDisplayType(ALL, sort)).willReturn(new ArrayList<>(Arrays.asList(outsiderPostToAll, friendPostToAll, defaultPostToAll)));
        given(postRepository.findAllByDisplayTypeAndAuthor(FRIEND, defaultFriend, sort)).willReturn(new ArrayList<>(Arrays.asList(friendPostToFriend)));
        given(postRepository.findAllByDisplayTypeAndAuthor(FRIEND, defaultUser, sort)).willReturn(new ArrayList<>(Arrays.asList(defaultPostToFriend)));
        given(postRepository.findAllByDisplayTypeAndAuthor(ONLY_ME, defaultUser, sort)).willReturn(new ArrayList<>(Arrays.asList(defaultPostToMe)));

        assertThat(postService.readAll(DEFAULT_USER_ID, sort)).isEqualTo(Arrays.asList(outsiderPostToAll, friendPostToFriend, friendPostToAll, defaultPostToMe, defaultPostToFriend, defaultPostToAll));
    }

    @Test
    void 좋아요를_누른상태에서_좋아요를_눌렀을_경우_확인() {
        PostLike postLike = new PostLike(defaultPost, defaultUser);
        given(postLikeRepository.findByPostAndUser(defaultPost, defaultUser)).willReturn(postLike);
        doNothing().when(postLikeRepository).delete(postLike);

        assertThat(postService.updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID)).isEqualTo(INIT_LIKE - 1);
    }

    @Test
    void 좋아요를_누르지_않은_상태에서_좋아요를_눌렀을_경우_확인() {
        given(postLikeRepository.findByPostAndUser(defaultPost, defaultUser)).willReturn(null);
        assertThat(postService.updateLike(DEFAULT_POST_ID, DEFAULT_USER_ID)).isEqualTo(INIT_LIKE + 1);
    }

    @Test
    @DisplayName("게시물 공유하는지 검증")
    void sharePost() {
        Post post = new Post(DEFAULT_CONTENT, defaultUser, defaultPost, ALL);
        given(postRepository.findById(1000L)).willReturn(Optional.of(post));
        given(sharedPostRepository.save(new SharedPost(defaultUser, post))).willReturn(new SharedPost(defaultUser, post));
        given(postRepository.save(post)).willReturn(post);
        assertThat(postService.share(postRequestDto, DEFAULT_USER_ID, 1000L)).isEqualTo(post);
    }
}
