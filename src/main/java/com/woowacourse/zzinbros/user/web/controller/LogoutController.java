package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.web.support.LoginSessionManager;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    private LoginSessionManager loginSessionManager;

    public LogoutController(LoginSessionManager loginSessionManager) {
        this.loginSessionManager = loginSessionManager;
    }

    @PostMapping
    public String logout(@SessionInfo UserSession userSessoin) {
        loginSessionManager.clearSession();
        return "redirect:/";
    }
}
