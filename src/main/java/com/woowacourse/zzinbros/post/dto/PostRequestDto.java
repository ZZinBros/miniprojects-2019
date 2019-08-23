package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostRequestDto that = (PostRequestDto) o;
        return Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contents);
    }
}
