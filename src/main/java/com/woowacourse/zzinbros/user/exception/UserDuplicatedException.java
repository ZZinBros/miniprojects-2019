package com.woowacourse.zzinbros.user.exception;

public class UserDuplicatedException extends UserException {

    public UserDuplicatedException() {
        super();
    }

    public UserDuplicatedException(String message) {
        super(message);
    }

    public UserDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDuplicatedException(Throwable cause) {
        super(cause);
    }

    public UserDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
