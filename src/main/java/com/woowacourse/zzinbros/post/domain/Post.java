package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne
    private User author;

    public Post() {
    }

    public Post(String contents, User author) {
        this.contents = contents;
        this.author = author;
    }

    public Post update(Post another) {
        if (matchAuthor(another.getAuthor())) {
            this.contents = another.contents;
            return this;
        }
        throw new UnAuthorizedException("게시글은 본인만 수정할 수 있습니다.");
    }

    public boolean matchAuthor(User user) {
        return this.author.equals(user);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updateDateTime;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
