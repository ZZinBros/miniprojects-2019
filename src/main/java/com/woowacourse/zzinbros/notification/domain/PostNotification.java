package com.woowacourse.zzinbros.notification.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;

import javax.persistence.*;

@Entity
public class PostNotification extends BaseEntity {
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id", nullable = false)
    private User publisher;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notified_user_id", nullable = false)
    private User notifiedUser;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "checked", nullable = false)
    private Boolean checked;

    public PostNotification(NotificationType type, User publisher, User notifiedUser, Post post) {
        this.type = type;
        this.publisher = publisher;
        this.notifiedUser = notifiedUser;
        this.post = post;
        checked = false;
    }

    public NotificationType getType() {
        return type;
    }

    public User getPublisher() {
        return publisher;
    }

    public User getNotifiedUser() {
        return notifiedUser;
    }

    public Post getPost() {
        return post;
    }

    public Boolean isChecked() {
        return checked;
    }

    public void checkNotification() {
        checked = true;
    }
}
