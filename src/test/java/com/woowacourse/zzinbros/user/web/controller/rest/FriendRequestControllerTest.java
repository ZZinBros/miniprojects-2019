package com.woowacourse.zzinbros.user.web.controller.rest;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FriendRequestControllerTest extends AuthedWebTestClient {

    @Test
    void getFriendRequests() {
        get("/friends-requests")
                .exchange()
                .expectStatus().isOk();
    }
}