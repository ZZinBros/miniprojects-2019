package com.woowacourse.zzinbros.user.dto;

import java.util.Objects;

public class LoginUserDto {
    public static final String LOGIN_USER = "loggedInUser";

    private Long id;
    private String name;
    private String email;

    public LoginUserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginUserDto)) return false;
        LoginUserDto that = (LoginUserDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
