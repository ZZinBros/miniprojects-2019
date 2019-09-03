package com.woowacourse.zzinbros.notification.domain;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.woowacourse.zzinbros.notification.domain.NotificationType.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

class PostNotificationTest extends BaseTest {
    @Test
    @DisplayName("알림을 checked 상태로 바꾼다.")
    void check() {
        PostNotification postNotification = new PostNotification(CREATED, new User(), new User(), new Post());

        postNotification.checkNotification();
        assertThat(postNotification.isChecked()).isTrue();
   }
}