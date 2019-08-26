package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.comment.AuthedWebTestClient;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class AcceptanceFriendControllerTest extends AuthedWebTestClient {

    @Test
    void 친구_추가() {
        post("/friends", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new FriendRequestDto(999L)), FriendRequestDto.class)
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueMatches("Location", ".*/posts\\?author=[0-9]*");
    }
}
