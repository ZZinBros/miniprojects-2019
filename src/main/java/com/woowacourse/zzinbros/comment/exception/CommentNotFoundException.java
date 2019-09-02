package com.woowacourse.zzinbros.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends CommentException {
    public CommentNotFoundException() {
        super();
    }
}
