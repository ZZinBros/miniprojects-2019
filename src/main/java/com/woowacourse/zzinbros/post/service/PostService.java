package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post add(PostRequestDto dto, User user) {
        Post post = new Post(dto.getContents(), user);
        return postRepository.save(post);
    }

    public Post update(long postId, PostRequestDto dto, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        return post.update(new Post(dto.getContents(), user));
    }

    public Post read(long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    public boolean delete(long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        if (post.matchAuthor(user)) {
            postRepository.delete(post);
            return true;
        }
        throw new UnAuthorizedException("작성자만 삭제할 수 있습니다.");
    }
}
