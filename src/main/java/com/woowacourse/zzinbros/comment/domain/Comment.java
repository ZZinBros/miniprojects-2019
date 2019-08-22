package com.woowacourse.zzinbros.comment.domain;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Lob
    @Column(nullable = false)
    private String contents;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

    public Comment() {
    }

    public Comment(final User author, final Post post, final String contents) {
        this.author = author;
        this.post = post;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public Post getPost() {
        return post;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void update(final String contents) {
        this.contents = contents;
    }

    public void prepareToDelete() {
        this.author = null;
        this.post = null;
    }

    public boolean isMatchUser(final User user) {
        return this.author.equals(user);
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        final Comment comment = (Comment) another;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, post, contents);
    }

    @Override
    public String toString() {
        return "Comment {" +
                "id: " + id +
                ", author: " + author +
                ", post: " + post +
                ", contents: \"" + contents + "\"" +
                ", createdDateTime: " + createdDateTime +
                ", updatedDateTime: " + updatedDateTime +
                "}";
    }
}
