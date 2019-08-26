package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.comment.AuthedWebTestClient;
import org.junit.jupiter.api.Test;

public class AcceptancePostPageControllerTest extends AuthedWebTestClient {

    @Test
    void 개인_글_요청() {
        get("/posts?author=777")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void 다른_유저_글_요청() {
        get("/posts?author=9898")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");
    }
}
