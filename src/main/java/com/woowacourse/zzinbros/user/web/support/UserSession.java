package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.springframework.lang.NonNull;

public class UserSession {
    public static final String LOGIN_USER = "loggedInUser";

    private UserResponseDto loginUserDto;

    UserSession(UserResponseDto loginUserDto) {
        this.loginUserDto = loginUserDto;
    }

    public boolean matchId(Long id) {
        return (id.compareTo(loginUserDto.getId()) == 0);
    }

    @NonNull
    public UserResponseDto getDto() {
        return new UserResponseDto(loginUserDto.getId(), loginUserDto.getName(), loginUserDto.getEmail());
    }
}
