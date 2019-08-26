package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.comment.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class AcceptanceLogoutControllerTest extends AuthedWebTestClient {
    @Test
    void 정상_로그아웃() {
        post("/logout", MediaType.APPLICATION_FORM_URLENCODED)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");
    }

    @Test
    void 로그인_없이_로그아웃() {
        webTestClient.post().uri("/logout")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance.*");

    }
}
