package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class AcceptanceFriendControllerTest extends AuthedWebTestClient {

    @Test
    @DisplayName("친구 조회")
    void getFriends() {
        get("/friends")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 친구_추가() {
        post("/friends", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new FriendRequestDto(999L)), FriendRequestDto.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", ".*/posts\\?author=[0-9]*");
    }

    @Test
    @DisplayName("친구 삭제")
    void friendDelete() {
        delete("/friends/444")
                .exchange()
                .expectStatus().isNoContent();
    }
}
