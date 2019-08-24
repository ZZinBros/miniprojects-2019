package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.PostLike;
import com.woowacourse.zzinbros.post.domain.repository.PostLikeRepository;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
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

    public PostService(UserService userService, PostRepository postRepository, PostLikeRepository postLikeRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
    }

    public Post add(PostRequestDto dto, long userId) {
        User user = userService.findUserById(userId);
        Post post = dto.toEntity(user);
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

    public List<Post> readAll() {
        return Collections.unmodifiableList(postRepository.findAll(new Sort(Sort.Direction.DESC, "createdDateTime")));
    }

    public List<Post> readAllByUser(User user) {
        return Collections.unmodifiableList(postRepository.findAllByAuthor(user));
    }

    @Transactional
    public int updateLike(long postId, long userId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userService.findUserById(userId);
        PostLike postLike = postLikeRepository.findByPostAndUser(post, user);
        if (Objects.isNull(postLike)) {
            postLike = new PostLike(post, user);
            post.addLike(postLike);
            return post.getPostLikes().size();
        }
        postLikeRepository.delete(postLike);
        post.removeLike(postLike);
        return post.getPostLikes().size();
    }
}
