package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.domain.User;
import org.springframework.stereotype.Component;

@Component
@Deprecated
public class PostDataAssembler {
    public PostResponseDto toResponse(Post entity, UserResponseDto userResponseDto) {
        return new PostResponseDto(entity.getId(),
                entity.getContents(),
                entity.getCreateDateTime(),
                entity.getUpdateDateTime(),
                userResponseDto);
    }

    public Post toEntity(PostRequestDto request, User user) {
        return new Post(request.getContents(), user);
    }
}
