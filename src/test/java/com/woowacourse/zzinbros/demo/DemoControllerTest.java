package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoControllerTest extends AuthedWebTestClient {

    @Test
    @DisplayName("로그인 되지 않았을 때, /entrance로 get요청하면 entrance페이지를 받아온다.")
    void enter() throws Exception {
        webTestClient.get().uri("/entrance")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    String body = new String(response.getResponseBody());
                    assertThat(body).contains("가입하기");
                });
    }

    @Test
    @DisplayName("로그인 안됐을 때 index로 get요청하면 /entrance로 redirect한다")
    void demoWhenNotLogin() throws Exception {
        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*/entrance.*");
    }

    @Test
    @DisplayName("로그인 됐을 때 index로 이동")
    void demoWhenLogin() throws Exception {
        get("/").exchange()
                .expectStatus().isOk();
    }

    @Test
    @DisplayName("로그인 되지 않았을 때, /entrance로 get요청하면 entrance페이지를 받아온다.")
    void enterFailIfLogIn() throws Exception {
        //TODO : 테스트 작성하여야 함
    }

    @Test
    @DisplayName("서버에서 시간 정보를 받아와, 시간 차이가 1초 내인지 확인한다.")
    void time() {
        webTestClient.get().uri("/time")
                .exchange()
                .expectStatus().isOk()
                .expectBody().consumeWith(response -> {
                    final String body = new String(Objects.requireNonNull(response.getResponseBody()))
                            .replaceAll("^.|.$", "");
                    final OffsetDateTime serverTime = OffsetDateTime.parse(body);
                    final Duration duration = Duration.between(serverTime, OffsetDateTime.now());
                    assertThat(duration).isLessThanOrEqualTo(Duration.ofSeconds(1));
        });
    }
}