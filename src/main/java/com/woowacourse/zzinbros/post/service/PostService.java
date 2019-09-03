package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.notification.domain.NotificationType;
import com.woowacourse.zzinbros.post.domain.DisplayType;
import com.woowacourse.zzinbros.notification.service.NotificationService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.post.domain.SharedPost;
import com.woowacourse.zzinbros.post.domain.repository.PostLikeRepository;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.domain.repository.SharedPostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.CREATED;

@Service
public class PostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final UserService userService;
    private final FriendService friendService;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final SharedPostRepository sharedPostRepository;
    private final NotificationService notificationService;

    public PostService(UserService userService,
                       FriendService friendService,
                       PostRepository postRepository,
                       PostLikeRepository postLikeRepository,
                       SharedPostRepository sharedPostRepository,
                       NotificationService notificationService) {
        this.userService = userService;
        this.friendService = friendService;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.sharedPostRepository = sharedPostRepository;
        this.notificationService = notificationService;
    }

    public Post add(PostRequestDto dto, long userId) {
        User user = userService.findUserById(userId);
        Post post = dto.toEntity(user);
        Post createdPost = postRepository.save(post);

        notify(createdPost, CREATED);
        return createdPost;
    }

    private void notify(Post post, NotificationType notificationType) {
        try {
            notificationService.notify(post, notificationType);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Transactional
    public Post share(PostRequestDto dto, long userId, long sharedPostId) {
        User user = userService.findUserById(userId);
        Post sharedPost = postRepository.findById(sharedPostId).orElseThrow(PostNotFoundException::new);
        sharedPost.share();

        sharedPostRepository.save(new SharedPost(user, sharedPost));
        Post post = dto.toEntity(user, sharedPost);

        return postRepository.save(post);
    }

    @Transactional
    public Post update(long postId, PostRequestDto dto, long userId) {
        User user = userService.findUserById(userId);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.update(dto.toEntity(user));
    }

    public Post read(long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public boolean delete(long postId, long userId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.findUserById(userId);
        if (post.matchAuthor(user)) {
            postRepository.delete(post);
            return true;
        }
        throw new UnAuthorizedException("작성자만 삭제할 수 있습니다.");
    }

    public List<Post> readAll(long userId, Sort sort) {
        User loginUser = userService.findUserById(userId);
        Set<User> friends = friendService.findFriendEntitiesByUser(userId);
        List<Post> posts = postRepository.findAllByDisplayType(DisplayType.ALL, sort);

        for (User friend : friends) {
            posts.addAll(postRepository.findAllByDisplayTypeAndAuthor(DisplayType.FRIEND, friend, sort));
        }

        posts.addAll(postRepository.findAllByDisplayTypeAndAuthor(DisplayType.FRIEND, loginUser, sort));
        posts.addAll(postRepository.findAllByDisplayTypeAndAuthor(DisplayType.ONLY_ME, loginUser, sort));

        posts.sort(Comparator.comparing(Post::getCreatedDateTime).reversed());
        return Collections.unmodifiableList(posts);
    }

    public List<Post> readAllByUser(User user, long loginUserId, Sort sort) {
        List<Post> posts = new ArrayList<>();
        if (user.getId() == loginUserId) {
            posts.addAll(postRepository.findAllByAuthor(user, sort));
            return Collections.unmodifiableList(posts);
        }

        posts.addAll(postRepository.findAllByDisplayTypeAndAuthor(DisplayType.ALL, user, sort));
        if (friendService.isMyFriend(loginUserId, user.getId())) {
            posts.addAll(postRepository.findAllByDisplayTypeAndAuthor(DisplayType.FRIEND, user, sort));
        }

        posts.sort(Comparator.comparing(Post::getCreatedDateTime).reversed());
        return Collections.unmodifiableList(posts);
    }

    @Transactional
    public int updateLike(long postId, long userId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.findUserById(userId);
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user);
        if (Objects.isNull(postLike)) {
            post.addLike();
            postLikeRepository.save(new PostLike(post, user));
            return post.getCountOfLike();
        }
        postLikeRepository.delete(postLike);
        post.removeLike();
        return post.getCountOfLike();
    }
}
