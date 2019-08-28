package com.woowacourse.zzinbros.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ResponseStatusException {
    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }
}
