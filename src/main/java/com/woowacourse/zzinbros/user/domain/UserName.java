package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.exception.IllegalUserArgumentException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class UserName {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 50;
    private static final String NAME_LENGTH_ERROR_MESSAGE = "길이가 %d 이상 %d 미만이어야 합니다";

    private String name;

    private UserName() {
    }

    public UserName(String name) {
        this.name = validateLength(name);
    }

    private String validateLength(String name) {
        if (!matchLength(name)) {
            String message = String.format(NAME_LENGTH_ERROR_MESSAGE, MIN_NAME_LENGTH, MAX_NAME_LENGTH);
            throw new IllegalUserArgumentException(message);
        }
        return name;
    }

    private boolean matchLength(String input) {
        return (input.length() >= MIN_NAME_LENGTH && input.length() < MAX_NAME_LENGTH);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserName)) return false;
        UserName userName = (UserName) o;
        return Objects.equals(name, userName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
