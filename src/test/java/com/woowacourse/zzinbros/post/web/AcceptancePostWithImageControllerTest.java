package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.util.Arrays;

class AcceptancePostWithImageControllerTest extends AuthedWebTestClient {

    private ByteArrayResource testResource = new ByteArrayResource(new byte[]{1, 2, 3}) {
        @Override
        public String getFilename() {
            return "test.png";
        }
    };

    @Test
    void 이미지_포함_포스트_등록() {
        multipartFilePost("/posts-with-images",
                Arrays.asList("contents", "feed-image"),
                "Contents", testResource)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}