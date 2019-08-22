package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.mediafile.MediaFile;
import com.woowacourse.zzinbros.mediafile.MediaFileRepository;
import com.woowacourse.zzinbros.mediafile.web.support.UploadTo;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PostWithImageService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final MediaFileRepository mediaFileRepository;

    public PostWithImageService(UserService userService, PostRepository postRepository, MediaFileRepository mediaFileRepository) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.mediaFileRepository = mediaFileRepository;
    }

    public Post add(PostRequestDto dto, long userId, UploadTo uploadToLocal) {
        User user = userService.findUserById(userId);
        Post post = dto.toEntity(user);
        MediaFile mediaFile = mediaFileRepository.save(new MediaFile(uploadToLocal.save()));
        post.addMediaFiles(mediaFile);
        postRepository.save(post);
        return post;
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
        return Collections.unmodifiableList(postRepository.findAll());
    }
}
