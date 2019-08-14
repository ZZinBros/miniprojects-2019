package com.woowacourse.zzinbros.user.exception;

public class NotValidUserException extends UserException {
    public NotValidUserException() {
    }

    public NotValidUserException(String message) {
        super(message);
    }

    public NotValidUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidUserException(Throwable cause) {
        super(cause);
    }

    public NotValidUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
