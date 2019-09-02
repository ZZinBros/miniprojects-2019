package com.woowacourse.zzinbros.post.exception;

public class PostNotFoundException extends PostException {
    public PostNotFoundException() {
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
