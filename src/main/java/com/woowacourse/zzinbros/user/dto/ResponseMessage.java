package com.woowacourse.zzinbros.user.dto;

public class ResponseMessage<T> {
    private T object;
    private String message;

    private ResponseMessage(T object, String message) {
        this.object = object;
        this.message = message;
    }

    public static <T> ResponseMessage of(T object, String message) {
        return new ResponseMessage<>(object, message);
    }

    public T getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }
}
