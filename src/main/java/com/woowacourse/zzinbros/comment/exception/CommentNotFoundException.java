package com.woowacourse.zzinbros.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends Exception {
    public CommentNotFoundException() {
    }

    public CommentNotFoundException(final String message) {
        super(message);
    }
}
