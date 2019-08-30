package com.woowacourse.zzinbros.notification.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.user.domain.User;

import javax.persistence.*;

@Entity
public class PostNotification extends BaseEntity {
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id")
    private User publisher;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notified_id")
    private User notified;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    public PostNotification(NotificationType type, User publisher, User notified, Long postId) {
        this.type = type;
        this.publisher = publisher;
        this.notified = notified;
        this.postId = postId;
    }

    public NotificationType getType() {
        return type;
    }

    public User getPublisher() {
        return publisher;
    }

    public User getNotified() {
        return notified;
    }

    public Long getPostId() {
        return postId;
    }
}
