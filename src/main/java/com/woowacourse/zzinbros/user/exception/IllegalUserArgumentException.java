package com.woowacourse.zzinbros.user.exception;

public class IllegalUserArgumentException extends UserException {
    public IllegalUserArgumentException() {
    }

    public IllegalUserArgumentException(String message) {
        super(message);
    }

    public IllegalUserArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalUserArgumentException(Throwable cause) {
        super(cause);
    }

    public IllegalUserArgumentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
