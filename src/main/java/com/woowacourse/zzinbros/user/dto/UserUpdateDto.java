package com.woowacourse.zzinbros.user.dto;

import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.user.domain.User;

import java.util.Objects;

public class UserUpdateDto {
    private String name;
    private String email;

    public UserUpdateDto() {
    }

    public UserUpdateDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User toEntity(String password) {
        return new User(name, email, password);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserUpdateDto)) return false;
        UserUpdateDto that = (UserUpdateDto) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail());
    }
}
