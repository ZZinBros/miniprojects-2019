package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.comment.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.BodyInserters;

public class AcceptanceLoginControllerTest extends AuthedWebTestClient {

    @Test
    void 로그인_성공() {
        webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "12345678")
                        .with("name", "test"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/;jsessionid=.*");
    }

    @Test
    void 로그인_실패() {
        webTestClient.post().uri("/login")
                .body(BodyInserters.fromFormData("email", "hack@hack.com")
                        .with("password", "12345678")
                        .with("name", "hack"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance");
    }
}
