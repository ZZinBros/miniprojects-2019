package com.woowacourse.zzinbros.user.web.exception;

import com.woowacourse.zzinbros.user.exception.UserException;

public class UserRegisterException extends UserException {

    public UserRegisterException() {
        super();
    }

    public UserRegisterException(String message) {
        super(message);
    }

    public UserRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRegisterException(Throwable cause) {
        super(cause);
    }

    public UserRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
