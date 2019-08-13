package com.woowacourse.zzinbros.user;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
public class User {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 10;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 30;
    private static final String EMAIL_PATTERN = "^.+@.+$";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = MAX_PASSWORD_LENGTH)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedTime;

    public User(String name, @Email String email, String password) {
        validateName(name);
        validateEmail(email);
        validatePassword(password);

        this.name = name;
        this.email = email;
        this.password = password;
    }

    private void validateName(String name) {
        if (!matchLength(name, MIN_NAME_LENGTH, MAX_NAME_LENGTH)) {
            throw new IllegalUserArgumentException();
        }
    }

    private void validatePassword(String password) {
        if (!matchLength(password, MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH)) {
            throw new IllegalUserArgumentException();
        }
    }

    private void validateEmail(String email) {
        if (!matchRegex(email, EMAIL_PATTERN)) {
            throw new IllegalUserArgumentException();
        }
    }

    private boolean matchLength(String input, int min, int max) {
        return (input.length() > min && input.length() < max);
    }

    private boolean matchRegex(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
