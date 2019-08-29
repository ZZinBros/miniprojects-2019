package com.woowacourse.zzinbros.post.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.post.exception.UnAuthorizedException;
import com.woowacourse.zzinbros.user.domain.User;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@DynamicInsert
public class Post extends BaseEntity {
    @Lob
    private String contents;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "post_to_user"))
    private User author;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_file_id", foreignKey = @ForeignKey(name = "post_to_media_file"))
    private MediaFile mediaFiles;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shared_post_id")
    private Post sharedPost;

    @ColumnDefault("0")
    private Integer countOfLike;

    @ColumnDefault("0")
    private Integer countOfShared;

    public Post() {
    }

    public Post(String contents, User author) {
        this.contents = contents;
        this.author = author;
        this.countOfLike = 0;
        this.countOfShared = 0;
    }

    public Post(String contents, User author, Post sharedPost) {
        this.contents = contents;
        this.author = author;
        this.sharedPost = sharedPost;
        this.countOfLike = 0;
        this.countOfShared = 0;
    }

    public Post update(Post post) {
        if (matchAuthor(post.author)) {
            this.contents = post.contents;
            return this;
        }
        throw new UnAuthorizedException("게시글은 본인만 수정할 수 있습니다.");
    }

    public boolean matchAuthor(User user) {
        return this.author.isAuthor(user);
    }

    public void addMediaFiles(MediaFile mediaFile) {
        this.mediaFiles = mediaFile;
    }

    public void addLike() {
        countOfLike++;
    }

    public void removeLike() {
        countOfLike--;
    }

    public void share() {
        this.countOfShared++;
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

    public MediaFile getMediaFiles() {
        return mediaFiles;
    }

    public int getCountOfLike() {
        if (countOfLike == null) {
            return 0;
        }
        return countOfLike;
    }

    public Post getSharedPost() {
        return sharedPost;
    }

    public Integer getCountOfShared() {
        return countOfShared;
    }
}
