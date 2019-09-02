package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.exception.IllegalUserArgumentException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserPassword {
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final String PASSWORD_LENGTH_ERROR_MESSAGE = "길이가 %d 이상 %d 미만이어야 합니다";

    private String password;

    private UserPassword() {
    }

    public UserPassword(String password) {
        this.password = validateLength(password);
    }

    private String validateLength(String password) {
        if (!matchLength(password)) {
            String message = String.format(PASSWORD_LENGTH_ERROR_MESSAGE,
                    MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);
            throw new IllegalUserArgumentException(message);
        }
        return password;
    }

    private boolean matchLength(String input) {
        return (input.length() >= MIN_PASSWORD_LENGTH && input.length() < MAX_PASSWORD_LENGTH);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserPassword)) return false;
        UserPassword that = (UserPassword) o;
        return Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
