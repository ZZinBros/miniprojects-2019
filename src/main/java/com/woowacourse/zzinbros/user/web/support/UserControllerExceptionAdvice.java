package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.exception.UserNotLoggedInException;
import com.woowacourse.zzinbros.user.web.exception.UserEditPageNotFoundException;
import com.woowacourse.zzinbros.user.web.exception.UserRegisterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class UserControllerExceptionAdvice {

    @ExceptionHandler({UserRegisterException.class, UserNotLoggedInException.class})
    public String handleUserException(Exception e, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/entrance";
    }

    @ExceptionHandler(UserEditPageNotFoundException.class)
    public String handleUserEditPageNotFoundException(UserException e) {
        return "redirect:/";
    }
}
