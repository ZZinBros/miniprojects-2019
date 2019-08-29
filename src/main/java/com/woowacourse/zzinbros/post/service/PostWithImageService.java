package com.woowacourse.zzinbros.post.service;

import com.woowacourse.zzinbros.common.config.upload.UploadTo;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.mediafile.service.MediaFileService;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.repository.PostRepository;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostWithImageService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final MediaFileService mediaFileService;

    public PostWithImageService(UserService userService, PostRepository postRepository, MediaFileService mediaFileService) {
        this.userService = userService;
        this.postRepository = postRepository;
        this.mediaFileService = mediaFileService;
    }

    public Post add(PostRequestDto dto, long userId, UploadTo uploadTo) {
        User user = userService.findUserById(userId);
        Post post = dto.toEntity(user);
        MediaFile mediaFile = mediaFileService.register(uploadTo);
        post.addMediaFiles(mediaFile);
        postRepository.save(post);
        return post;
    }
}
