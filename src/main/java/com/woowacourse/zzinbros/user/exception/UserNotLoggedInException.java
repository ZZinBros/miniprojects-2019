package com.woowacourse.zzinbros.user.exception;

public class UserNotLoggedInException extends UserException {

    public UserNotLoggedInException() {
        super();
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
