package com.woowacourse.zzinbros.user.exception;

public class UserNotLoggedInException extends UserException {

    public UserNotLoggedInException() {
        super();
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }

    public UserNotLoggedInException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotLoggedInException(Throwable cause) {
        super(cause);
    }

    public UserNotLoggedInException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
