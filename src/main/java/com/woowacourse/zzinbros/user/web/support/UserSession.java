package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.dto.LoginUserDto;
import org.springframework.lang.NonNull;

public class UserSession {
    private LoginUserDto loginUserDto;

    UserSession(LoginUserDto loginUserDto) {
        this.loginUserDto = loginUserDto;
    }

    public boolean matchId(Long id) {
        return (id.compareTo(loginUserDto.getId()) == 0);
    }

    @NonNull
    public LoginUserDto getDto() {
        return new LoginUserDto(loginUserDto.getId(), loginUserDto.getName(), loginUserDto.getEmail());
    }
}
