package com.woowacourse.zzinbros.post.dto;

import com.woowacourse.zzinbros.post.domain.Post;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String contents;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
    private UserResponseDto userResponseDto;

    public PostResponseDto() {
    }

    public PostResponseDto(Long id,
                           String contents,
                           LocalDateTime createDateTime,
                           LocalDateTime updateDateTime,
                           UserResponseDto userResponseDto) {
        this.id = id;
        this.contents = contents;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.userResponseDto = userResponseDto;
    }

    public PostResponseDto(Post post, UserResponseDto userResponseDto) {
        this(post.getId(),
                post.getContents(),
                post.getCreateDateTime(),
                post.getUpdateDateTime(),
                userResponseDto);
    }

    public UserResponseDto getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(UserResponseDto userResponseDto) {
        this.userResponseDto = userResponseDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(LocalDateTime updateDateTime) {
        this.updateDateTime = updateDateTime;
    }
}
