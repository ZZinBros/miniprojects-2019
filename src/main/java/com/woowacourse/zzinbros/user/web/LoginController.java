package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public String login(UserRequestDto userRequestDto, HttpSession session) {
        try {
            UserSession userSession = userService.login(userRequestDto);
            session.setAttribute(UserSession.LOGIN_USER, userSession);
            return "redirect:/";
        } catch (UserException e) {
            return "redirect:/login";
        }
    }
}
