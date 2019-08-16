package com.woowacourse.zzinbros.user.domain;

public class ResponseMessage<T> {
    private T object;
    private String message;

    public ResponseMessage(T object, String message) {
        this.object = object;
        this.message = message;
    }

    public T getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }
}
