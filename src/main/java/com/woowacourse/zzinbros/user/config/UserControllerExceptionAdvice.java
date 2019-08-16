package com.woowacourse.zzinbros.user.config;

import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.web.exception.UserRegisterException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerExceptionAdvice {

    @ExceptionHandler(UserRegisterException.class)
    public String handleUserException(UserException e, Model model) {
        return "redirect:/signup";
    }
}
