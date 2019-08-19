package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.dto.LoginUserDto;

public class UserSession {
    private LoginUserDto loginUserDto;

    public UserSession(LoginUserDto loginUserDto) {
        this.loginUserDto = loginUserDto;
    }

    public boolean matchId(Long id) {
        return (id.compareTo(loginUserDto.getId()) == 0);
    }

    public LoginUserDto toDto() {
        return loginUserDto;
    }
}
