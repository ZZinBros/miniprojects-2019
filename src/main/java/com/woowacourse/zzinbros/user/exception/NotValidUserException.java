package com.woowacourse.zzinbros.user.exception;

public class NotValidUserException extends UserException {
    public NotValidUserException() {
    }

    public NotValidUserException(String message) {
        super(message);
    }
}
