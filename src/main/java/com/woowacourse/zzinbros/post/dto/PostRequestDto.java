package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.DisplayStrategy;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;

import java.util.Objects;

public class PostRequestDto {
    private String contents;
    private long sharedPostId;
    private int displayStrategy;

    public PostRequestDto() {
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public long getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(long sharedPostId) {
        this.sharedPostId = sharedPostId;
    }

    public int getDisplayStrategy() {
        return displayStrategy;
    }

    public void setDisplayStrategy(int displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public Post toEntity(User user) {
        return new Post(contents, user, DisplayStrategy.valueOf(displayStrategy));
    }

    public Post toEntity(User user, Post sharedPost) {
        return new Post(contents, user, sharedPost);
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
