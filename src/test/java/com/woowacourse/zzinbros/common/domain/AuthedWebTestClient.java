package com.woowacourse.zzinbros.common.domain;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthedWebTestClient extends BaseTest {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;

    private String loginCookie() {
        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "12345678"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
        return cookie;
    }

    protected WebTestClient.RequestBodySpec post(String uri, MediaType mediaType) {
        String cookie = loginCookie();
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(mediaType);
    }

    protected WebTestClient.RequestBodySpec put(String uri, MediaType mediaType) {
        String cookie = loginCookie();
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(mediaType);
    }

    protected WebTestClient.RequestHeadersSpec get(String uri) {
        String cookie = loginCookie();
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec delete(String uri) {
        String cookie = loginCookie();
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec multipartFilePost(String uri, List<String> keys, Object... parameters) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        for (int i = 0; i < keys.size(); i++) {
            bodyBuilder.part(keys.get(i), parameters[i]);
        }
        String cookie = loginCookie();
        webTestClient = webTestClient.mutate().responseTimeout(Duration.ofSeconds(10)).build();
        return webTestClient.post().uri(uri)
                .header("Cookie", cookie)
                .syncBody(bodyBuilder.build());
    }

    protected WebTestClient.RequestHeadersSpec multipartFilePut(String uri, List<String> keys, Object... parameters) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        for (int i = 0; i < keys.size(); i++) {
            bodyBuilder.part(keys.get(i), parameters[i]);
        }
        String cookie = loginCookie();
        webTestClient = webTestClient.mutate().responseTimeout(Duration.ofSeconds(10)).build();
        return webTestClient.put().uri(uri)
                .header("Cookie", cookie)
                .syncBody(bodyBuilder.build());
    }
}