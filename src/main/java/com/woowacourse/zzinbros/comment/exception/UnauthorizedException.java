package com.woowacourse.zzinbros.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
    }

    public UnauthorizedException(final String message) {
        super(message);
    }
}
