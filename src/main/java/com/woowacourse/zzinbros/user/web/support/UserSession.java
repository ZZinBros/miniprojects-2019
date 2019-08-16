package com.woowacourse.zzinbros.user.web.support;

import java.util.Objects;

public class UserSession {
    public static final String LOGIN_USER = "loggedInUser";

    private Long id;
    private String name;
    private String email;

    public UserSession(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public boolean matchId(Long id) {
        return (this.id.compareTo(id) == 0);
    }

    public long getId() {
        return id;
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
