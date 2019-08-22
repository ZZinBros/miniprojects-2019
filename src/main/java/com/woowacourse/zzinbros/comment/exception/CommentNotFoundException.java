package com.woowacourse.zzinbros.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
    }

    public CommentNotFoundException(final String message) {
        super(message);
    }
}
