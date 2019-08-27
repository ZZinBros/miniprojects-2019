package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.post.domain.SharingUser;
import com.woowacourse.zzinbros.post.domain.repository.PostLikeRepository;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.domain.repository.SharingUserRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final SharingUserRepository sharingUserRepository;

    public PostService(UserService userService,
                       PostRepository postRepository,
                       PostLikeRepository postLikeRepository,
                       SharingUserRepository sharingUserRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.sharingUserRepository = sharingUserRepository;
    }

    public Post add(PostRequestDto dto, long userId) {
        User user = userService.findUserById(userId);
        Post post = dto.toEntity(user);
        return postRepository.save(post);
    }

    @Transactional
    public Post add(PostRequestDto dto, long userId, long sharedPostId) {
        User user = userService.findUserById(userId);
        Post sharedPost = postRepository.findById(sharedPostId).orElseThrow(PostNotFoundException::new);
        sharedPost.share();

        SharingUser sharingUser = new SharingUser(user, sharedPost);
        sharingUserRepository.save(sharingUser);

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

    public List<Post> readAll(Sort sort) {
        return Collections.unmodifiableList(postRepository.findAll(sort));
    }

    public List<Post> readAllByUser(User user, Sort sort) {
        return Collections.unmodifiableList(postRepository.findAllByAuthor(user, sort));
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
