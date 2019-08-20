package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;

public class PostRequestDto {
    private String contents;

    public PostRequestDto() {
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Post toEntity(User user) {
        return new Post(contents, user);
    }
}
