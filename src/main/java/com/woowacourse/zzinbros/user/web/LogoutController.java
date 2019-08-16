package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public String logout(HttpSession session, UserSession userSession) {
        session.removeAttribute(UserSession.LOGIN_USER);
        return "index";
    }
}
