package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.mediafile.web.support.AuthedWebTestClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;

import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostWithImageControllerTest extends AuthedWebTestClient {
    private ByteArrayResource testResource = new ByteArrayResource(new byte[]{1, 2, 3}) {
        @Override
        public String getFilename() {
            return "test.png";
        }
    };

    @Test
    void name() {
        multipartFilePost("/posts-with-images",
                Arrays.asList("contents", "feed-image"),
                "Contents", testResource)
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}