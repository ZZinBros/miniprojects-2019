package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import org.junit.jupiter.api.Test;

public class AcceptanceUserEditPageControllerTest extends AuthedWebTestClient {
    @Test
    void 자신의_유저_페이지_이동() {
        get("/users/777/edit")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 다른_유저_페이지_이동() {
        get("/users/999/edit")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");
    }
}
