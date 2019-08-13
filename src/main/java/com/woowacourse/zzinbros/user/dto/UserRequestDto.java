package com.woowacourse.zzinbros.user.dto;

import com.woowacourse.zzinbros.user.domain.User;

public class UserRequestDto {

    private final String name;
    private final String email;
    private final String password;

    public UserRequestDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User toEntity() {
        return new User(name, email, password);
    }
}
