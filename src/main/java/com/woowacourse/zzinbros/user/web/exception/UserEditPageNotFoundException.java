package com.woowacourse.zzinbros.user.web.exception;

import com.woowacourse.zzinbros.user.exception.UserException;

public class UserEditPageNotFoundException extends UserException {
    public UserEditPageNotFoundException() {
    }

    public UserEditPageNotFoundException(String message) {
        super(message);
    }

    public UserEditPageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEditPageNotFoundException(Throwable cause) {
        super(cause);
    }

    public UserEditPageNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
