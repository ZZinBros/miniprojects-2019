package com.woowacourse.zzinbros.mediafile.web.support;

import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AuthedWebTestClient {

    @Autowired
    protected WebTestClient webTestClient;
    @Autowired
    protected UserRepository userRepository;

    private String loginCookie() {
        String cookie = webTestClient.post().uri("/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("email", "test@test.com")
                        .with("password", "123456789"))
                .exchange()
                .returnResult(String.class).getResponseHeaders().getFirst("Set-Cookie");
        return cookie;
    }

    protected WebTestClient.RequestBodySpec post(String uri) {
        String cookie = loginCookie();
        return webTestClient.post()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestBodySpec put(String uri) {
        String cookie = loginCookie();
        return webTestClient.put()
                .uri(uri)
                .header("Cookie", cookie)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }

    protected WebTestClient.RequestHeadersSpec get(String uri) {
        String cookie = loginCookie();
        return webTestClient.get().uri(uri).header("Cookie", cookie);
    }

    protected WebTestClient.RequestHeadersSpec delete(String uri) {
        String cookie = loginCookie();
        return webTestClient.delete().uri(uri).header("Cookie", cookie);
    }

    protected <T> BodyInserters.FormInserter<String> params(Class<T> classType, String... parameters) {
        BodyInserters.FormInserter<String> body = BodyInserters.fromFormData(Strings.EMPTY, Strings.EMPTY);
        for (int i = 1; i < classType.getDeclaredFields().length; i++) {
            body.with(classType.getDeclaredFields()[i].getName(), parameters[i - 1]);
        }
        return body;
    }

    protected WebTestClient.RequestHeadersSpec multipartFilePost(String uri, List<String> keys, Object... paremeters) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        for (int i = 0; i < keys.size(); i++) {
            bodyBuilder.part(keys.get(i), paremeters[i]);
        }
        return webTestClient.post().uri(uri)
                .syncBody(bodyBuilder.build());
    }
}