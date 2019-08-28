package com.woowacourse.zzinbros.comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends ResponseStatusException {
    public CommentNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
