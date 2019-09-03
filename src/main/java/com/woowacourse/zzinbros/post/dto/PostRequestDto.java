package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.DisplayType;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;

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
        return new Post(contents, user, DisplayType.valueOf(displayStrategy));
    }

    public Post toEntity(User user, Post sharedPost) {
        return new Post(contents, user, sharedPost, DisplayType.valueOf(displayStrategy));
    }
}
