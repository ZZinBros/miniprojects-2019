package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class AcceptancePostControllerTest extends AuthedWebTestClient {

    @Test
    void 게시글_생성() {
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContents("post");
        post("/posts", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(postRequestDto), PostRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo("post");
    }

    @Test
    void 게시글_수정() {
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContents("update");
        put("/posts/777", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(postRequestDto), PostRequestDto.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.contents").isEqualTo("update");
    }

    @Test
    void 게시글_수정_권한없음() {
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContents("hacker");
        put("/posts/999", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(postRequestDto), PostRequestDto.class)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");
    }

    @Test
    void 게시글_삭제() {
        delete("/posts/888")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void 게시글_삭제_권한없음() {
        delete("/posts/999")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", ".*(/)");
    }

    @Test
    void 게시글_공유() {
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContents("post");
        postRequestDto.setSharedPostId(999L);
        post("/posts/share", MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(postRequestDto), PostRequestDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.contents").isEqualTo("post")
                .jsonPath("$.sharedPost.id").isEqualTo(999L);

        get("/posts/999")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.countOfShared").isEqualTo(1);
    }
}
