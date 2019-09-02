package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AcceptanceSearchControllerTest extends AuthedWebTestClient {
    @Test
    @DisplayName("정상 검색")
    void search() {
        get("/users?name=friend")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isNotEmpty();
    }
}
