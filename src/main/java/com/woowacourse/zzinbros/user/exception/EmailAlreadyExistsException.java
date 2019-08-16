package com.woowacourse.zzinbros.user.exception;

public class EmailAlreadyExistsException extends UserException {

    public EmailAlreadyExistsException() {
        super();
    }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
