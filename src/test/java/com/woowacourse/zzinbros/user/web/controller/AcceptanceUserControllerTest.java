package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.comment.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;

public class AcceptanceUserControllerTest extends AuthedWebTestClient {

    @Test
    void 유저_마이페이지() {
        get("/users/777")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("test");
    }

    @Test
    void 정상_회원가입() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", "abc@abc.com")
                        .with("password", "12345678")
                        .with("name", "abc"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance");
    }

    @Test
    void 비정상_회원가입() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "12345678")
                        .with("name", "abc"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance");
    }

    @Test
    void 개인정보_수정() {
        put("/users/777", MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "edited")
                        .with("email", "test@test.com")
                        .with("password", "12345678"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");

        put("/users/777", MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "test")
                        .with("email", "test@test.com")
                        .with("password", "12345678"))
                .exchange();
    }

    @Test
    void 다른_유저_정보_수정() {
        put("/users/999", MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("name", "edited")
                        .with("email", "test@test.com")
                        .with("password", "12345678"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/users/.*");
    }
}
