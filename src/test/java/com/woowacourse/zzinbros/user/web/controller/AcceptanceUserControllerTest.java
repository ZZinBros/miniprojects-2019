package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
                .expectHeader().valueMatches("Location", ".*/entrance.*");
    }

    @Test
    void 비정상_회원가입() {
        webTestClient.post().uri("/users")
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "12345678")
                        .with("name", "abc"))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance.*");
    }

    @Test
    void 개인정보_수정() {
        put("/users/777", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new UserUpdateDto("edited", "test@test.com")), UserUpdateDto.class)
                .exchange()
                .expectStatus().isOk();

        put("/users/777", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new UserUpdateDto("test", "test@test.com")), UserUpdateDto.class)
                .exchange();
    }

    @Test
    void 다른_유저_정보_수정() {
        put("/users/999", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new UserUpdateDto("edited", "test@test.com")), UserUpdateDto.class)
                .exchange()
                .expectStatus().isAccepted();
    }

    @Test
    @DisplayName("정상 검색")
    void search() {
        get("/users?name=friend")
                .exchange()
                .expectStatus().isOk();
    }
}
