package com.woowacourse.zzinbros.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class UserTest {
    public static final String BASE_NAME = "테스트";
    public static final String BASE_EMAIL = "test@test.com";
    public static final String BASE_PASSWORD = "123qweASD!";

    @Test
    @DisplayName("유저 이름이 제한길이를 초과했을때 예외를 던진다")
    public void userNameOverMaxLength() {
        final String invalidName = "열자이상의이름열자이상";
        assertThatThrownBy(() ->
                new User(invalidName, BASE_EMAIL, BASE_PASSWORD)).isInstanceOf(IllegalUserArgumentException.class);
    }

    @Test
    @DisplayName("비밀번호의 길이가 제한 길이 미만일때 예외를 던진다")
    public void userPasswordUnderMinLength() {
        final String invalidPassword = "1aA!";
        assertThatThrownBy(() ->
                new User(BASE_NAME, BASE_EMAIL, invalidPassword)).isInstanceOf(IllegalUserArgumentException.class);
    }

    @Test
    @DisplayName("비밀번호의 길이가 제한 길이 미만일때 예외를 던진다")
    public void userPasswordOverMaxLength() {
        final String invalidPassword = "1aA!aaaaaaaaaaaaaaaaaaaaaaaaaaa";
        assertThatThrownBy(() ->
                new User(BASE_NAME, BASE_EMAIL, invalidPassword)).isInstanceOf(IllegalUserArgumentException.class);
    }

    @Test
    @DisplayName("이메일 형식이 아닐때 예외를 던진다")
    public void invalidUserEmail() {
        final String invalidEmail = "test";
        assertThatThrownBy(() ->
                new User(BASE_NAME, invalidEmail, BASE_PASSWORD)).isInstanceOf(IllegalUserArgumentException.class);
    }

    @Test
    @DisplayName("유저를 업데이트한다")
    public void update() {
        User user = new User(BASE_NAME, BASE_EMAIL, BASE_PASSWORD);
        final String updatedName = "새 이름";
        final String updatedPassword = "newPassword!@";
        final String updatedEmail = "updated@test.com";

        User updatedUser = new User(updatedName, updatedEmail, updatedPassword);
        user.update(updatedUser);

        assertThat(user.getName()).isEqualTo(updatedName);
        assertThat(user.getEmail()).isEqualTo(updatedEmail);
        assertThat(user.getPassword()).isEqualTo(updatedPassword);
    }
}