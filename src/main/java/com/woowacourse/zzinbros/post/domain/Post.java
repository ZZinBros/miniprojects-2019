package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.mediafile.MediaFile;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String contents;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "post_to_user"))
    private User author;

    @OneToMany
    @JoinColumn(name = "media_file_id", foreignKey = @ForeignKey(name = "post_to_media_file"))
    private List<MediaFile> mediaFiles = new ArrayList<>();

    public Post() {
    }

    public Post(String contents, User author) {
        this.contents = contents;
        this.author = author;
    }

    public Post update(Post post) {
        if (matchAuthor(post.author)) {
            this.contents = post.contents;
            return this;
        }
        throw new UnAuthorizedException("게시글은 본인만 수정할 수 있습니다.");
    }

    public boolean matchAuthor(User user) {
        return this.author.equals(user);
    }

    public void addMediaFiles(MediaFile mediaFile) {
        this.mediaFiles.add(mediaFile);
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

    public List<MediaFile> getMediaFiles() {
        return new ArrayList<>(mediaFiles);
    }

    public void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles = mediaFiles;
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
