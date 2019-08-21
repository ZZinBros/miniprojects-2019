package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.LoginSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private UserService userService;
    private LoginSessionManager loginSessionManager;

    public LoginController(UserService userService, LoginSessionManager loginSessionManager) {
        this.userService = userService;
        this.loginSessionManager = loginSessionManager;
    }

    @PostMapping
    public String login(UserRequestDto userRequestDto) {
        try {
            UserResponseDto loginUserDto = userService.login(userRequestDto);
            loginSessionManager.setLoginSession(loginUserDto);
            return "redirect:/";
        } catch (UserException e) {
            return "redirect:/login";
        }
    }
}
