package com.woowacourse.zzinbros.user.config;

import com.woowacourse.zzinbros.user.exception.UserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public String handleUserException(UserException e) {
        return "redirect:/";
    }
}
