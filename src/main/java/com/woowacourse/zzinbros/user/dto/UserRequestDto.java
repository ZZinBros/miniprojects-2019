package com.woowacourse.zzinbros.user.dto;

import com.woowacourse.zzinbros.user.User;

public class UserRequestDto {

    private final String baseName;
    private final String baseEmail;
    private final String basePassword;

    public UserRequestDto(String baseName, String baseEmail, String basePassword) {
        this.baseName = baseName;
        this.baseEmail = baseEmail;
        this.basePassword = basePassword;
    }

    public User toEntity() {
        return new User(baseName, baseEmail, basePassword);
    }
}
