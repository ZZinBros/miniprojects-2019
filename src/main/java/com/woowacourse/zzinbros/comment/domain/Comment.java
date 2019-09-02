package com.woowacourse.zzinbros.comment.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
@DynamicUpdate
public class Comment extends BaseEntity {
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @Lob
    @Column(nullable = false)
    private String contents;

    protected Comment() {
    }

    public Comment(User author, Post post, String contents) {
        this.author = author;
        this.post = post;
        this.contents = contents;
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

    public void update(String contents) {
        this.contents = contents;
    }

    public void prepareToDelete() {
        this.author = null;
        this.post = null;
    }

    public boolean isAuthor(User user) {
        return this.author.isAuthor(user);
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
