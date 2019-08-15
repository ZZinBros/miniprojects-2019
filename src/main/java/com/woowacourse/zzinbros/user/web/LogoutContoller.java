package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class LogoutContoller {

    @GetMapping
    public String logout(HttpSession session) {
        session.removeAttribute(UserSession.LOGIN_USER);
        return "index";
    }
}
