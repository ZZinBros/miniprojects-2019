package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.exception.UserNotLoggedInException;
import com.woowacourse.zzinbros.user.web.exception.UserEditPageNotFoundException;
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

    @ExceptionHandler(UserEditPageNotFoundException.class)
    public String handleUserEditPageNotFoundException(UserException e) {
        return "redirect:/";
    }

    @ExceptionHandler(UserNotLoggedInException.class)
    public String handleUserNotLoggedInExcpetion(UserNotLoggedInException e) {
        return "redirect:/login";
    }
}
