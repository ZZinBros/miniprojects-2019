package com.woowacourse.zzinbros.user.web.support;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class LoginSessionManager {
    public static final String LOGIN_USER = "loggedInUser";

    private HttpServletRequest httpServletRequest;

    public LoginSessionManager(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setLoginSession(UserSession userSession) {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute(LOGIN_USER, userSession);
    }

    public void clearSession() {
        HttpSession session = httpServletRequest.getSession();
        session.removeAttribute(LOGIN_USER);
    }
}
