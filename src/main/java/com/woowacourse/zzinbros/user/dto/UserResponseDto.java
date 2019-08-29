package com.woowacourse.zzinbros.user.dto;

import java.io.Serializable;
import java.util.Objects;

public class UserResponseDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String profile;

    public UserResponseDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UserResponseDto(Long id, String name, String email, String profile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponseDto)) return false;
        UserResponseDto that = (UserResponseDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
