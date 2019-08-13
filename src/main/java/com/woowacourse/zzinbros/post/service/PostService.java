package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.UnAuthorizedException;
import com.woowacourse.zzinbros.post.domain.User;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.dto.PostResponseDto;
import com.woowacourse.zzinbros.post.dto.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto add(PostRequestDto dto, User user) {
        Post post = new Post(dto.getContents(), user);
        Post persistPost = postRepository.save(post);
        return new PostResponseDto(persistPost, new UserResponseDto(user.getId()));
    }

    public PostResponseDto update(long postId, PostRequestDto dto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Post updatePost = post.update(new Post(dto.getContents(), user));
        return new PostResponseDto(updatePost, new UserResponseDto(user.getId()));
    }

    public PostResponseDto read(long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return new PostResponseDto(post, new UserResponseDto(user.getId()));
    }

    public void delete(long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (post.matchAuthor(user)) {
            postRepository.delete(post);
            return;
        }
        throw new UnAuthorizedException("작성자만 삭제할 수 있습니다.");
    }
}
