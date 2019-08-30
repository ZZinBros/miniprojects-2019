package com.woowacourse.zzinbros.user.dto;

public class ModifyResponseMessage<T> {
    private T object;
    private String message;

    public ModifyResponseMessage(T object, String message) {
        this.object = object;
        this.message = message;
    }

    public static <T> ModifyResponseMessage of(T object, String message) {
        return new ModifyResponseMessage(object, message);
    }

    public T getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }
}
