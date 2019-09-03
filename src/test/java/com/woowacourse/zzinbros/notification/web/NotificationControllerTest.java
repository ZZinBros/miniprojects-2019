package com.woowacourse.zzinbros.notification.web;

import com.woowacourse.zzinbros.common.domain.AuthedWebTestClient;
import com.woowacourse.zzinbros.notification.dto.NotificationResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NotificationControllerTest extends AuthedWebTestClient {
    @Test
    @DisplayName("page단위로 유저에게 들어온 알림을 조회한다.")
    void fetch() {
        int pageOffset = 0;
        int pageSize = 3;
        String sort = "createdDateTime,asc";
        String requestUrl = "/notifications?page=" + pageOffset
                + "&size=" + pageSize
                + "&sort=" + sort;

        get(requestUrl)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(NotificationResponseDto.class)
                .hasSize(pageSize);
    }
}