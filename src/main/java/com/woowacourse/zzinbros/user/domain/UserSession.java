package com.woowacourse.zzinbros.user.domain;

import java.util.Objects;

public class UserSession {
    public static final String LOGIN_USER = "loggedInUser";

    private String name;
    private String email;

    public UserSession(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public boolean matchEmail(User user) {
        return this.email.equals(user.getEmail());
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSession)) return false;
        UserSession that = (UserSession) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}
