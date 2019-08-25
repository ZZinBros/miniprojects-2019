package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.mediafile.MediaFile;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@DynamicInsert
public class Post extends BaseEntity {
    @Lob
    private String contents;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "post_to_user"))
    private User author;

    @OneToMany
    @JoinColumn(name = "media_file_id", foreignKey = @ForeignKey(name = "post_to_media_file"))
    private List<MediaFile> mediaFiles = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<PostLike> postLikes = new HashSet<>();

    @Column
    @ColumnDefault("0")
    private Integer countOfLike;

    public Post() {
    }

    public Post(String contents, User author) {
        this.contents = contents;
        this.author = author;
        countOfLike = 0;
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

    public void addLike(PostLike postLike) {
        postLikes.add(postLike);
        countOfLike++;
    }

    public void removeLike(PostLike postLike) {
        postLikes.remove(postLike);
        countOfLike--;
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreateDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return updatedDateTime;
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

    public Set<PostLike> getPostLikes() {
        return Collections.unmodifiableSet(postLikes);
    }

    public int getCountOfLike() {
        if (countOfLike == null) {
            return 0;
        }
        return countOfLike;
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
