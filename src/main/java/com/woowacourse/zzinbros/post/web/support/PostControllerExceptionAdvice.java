package com.woowacourse.zzinbros.post.web.support;

import com.woowacourse.zzinbros.post.exception.PostException;
import com.woowacourse.zzinbros.post.exception.PostNotFoundException;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PostControllerExceptionAdvice {
    @ExceptionHandler(PostException.class)
    public String handlePostException(PostException e, Model model) {
        return "redirect:/";
    }

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(PostNotFoundException e, Model model) {
        return "redirect:/";
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public String handleUnAuthorizedException(UnAuthorizedException e, Model model) {
        return "redirect:/";
    }
}
