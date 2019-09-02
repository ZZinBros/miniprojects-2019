package com.woowacourse.zzinbros.post.exception;

public class UnAuthorizedException extends PostException {
    public UnAuthorizedException() {
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
