package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.web.reactive.function.BodyInserters.fromFormData;

public class ControllerTestUtil {

    static String login(WebTestClient webTestClient, UserRequestDto userRequestDto) {
        return webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(fromFormData("email", userRequestDto.getEmail())
                        .with("password", userRequestDto.getPassword()))
                .exchange()
                .returnResult(String.class)
                .getResponseCookies().get("JSESSIONID").get(0).getValue();
    }
}
