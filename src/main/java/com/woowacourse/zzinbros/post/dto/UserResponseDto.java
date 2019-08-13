package com.woowacourse.zzinbros.post.dto;

public class UserResponseDto {
    private Long id;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
