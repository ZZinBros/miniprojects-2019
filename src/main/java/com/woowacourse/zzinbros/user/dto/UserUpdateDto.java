package com.woowacourse.zzinbros.user.dto;

import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.user.domain.User;

public class UserUpdateDto {
    private String name;
    private String email;

    public UserUpdateDto() {
    }

    public UserUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User toEntity(String password, MediaFile mediaFile) {
        return new User(name, email, password, mediaFile);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
