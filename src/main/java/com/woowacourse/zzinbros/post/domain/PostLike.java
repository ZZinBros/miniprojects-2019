package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    protected PostLike() {
    }

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }
}
