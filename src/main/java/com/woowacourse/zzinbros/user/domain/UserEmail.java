package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.exception.IllegalUserArgumentException;

import javax.persistence.Embeddable;
import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class UserEmail {
    private static final String EMAIL_PATTERN_ERROR_MESSAGE = "이메일 형식으로 입력해주세요";
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^.+@.+$");

    private String email;

    private UserEmail() {
    }

    public UserEmail(String email) {
        this.email = validatePattern(email);
    }

    private String validatePattern(String email) {
        if (!matchRegex(email)) {
            throw new IllegalUserArgumentException(EMAIL_PATTERN_ERROR_MESSAGE);
        }
        return email;
    }

    private boolean matchRegex(String input) {
        return EMAIL_PATTERN.matcher(input).find();
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEmail)) return false;
        UserEmail userEmail = (UserEmail) o;
        return Objects.equals(email, userEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
