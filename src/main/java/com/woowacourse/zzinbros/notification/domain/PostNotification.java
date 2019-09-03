package com.woowacourse.zzinbros.notification.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.user.domain.User;

import javax.persistence.*;

@Entity
public class PostNotification extends BaseEntity {
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @Column(name = "notified_user_id", nullable = false)
    private Long notifiedUserId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public PostNotification(NotificationType type, User publisher, Long notifiedUserId, Long postId) {
        this.type = type;
        this.publisher = publisher;
        this.notifiedUserId = notifiedUserId;
        this.postId = postId;
    }

    public NotificationType getType() {
        return type;
    }

    public User getPublisher() {
        return publisher;
    }

    public Long getNotifiedUserId() {
        return notifiedUserId;
    }

    public Long getPostId() {
        return postId;
    }
}
