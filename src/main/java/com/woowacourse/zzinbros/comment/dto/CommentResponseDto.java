package com.woowacourse.zzinbros.comment.dto;

import com.woowacourse.zzinbros.comment.domain.Comment;
import com.woowacourse.zzinbros.common.EscapedCharacters;

import java.time.OffsetDateTime;
import java.util.Optional;

public class CommentResponseDto {
    private Long commentId;
    private Long authorId;
    private String authorName;
    private String contents;
    private String errorMessage;
    private OffsetDateTime createdDateTime;
    private OffsetDateTime updatedDateTime;
    private String profile;

    public CommentResponseDto() {
    }

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.authorId = comment.getAuthor().getId();
        this.authorName = comment.getAuthor().getName();
        this.contents = comment.getContents();
        this.createdDateTime = comment.getCreatedDateTime();
        this.updatedDateTime = comment.getUpdatedDateTime();
        this.profile = comment.getAuthor().getProfile().getUrl();
    }

    public CommentResponseDto(Exception exception) {
        this.errorMessage = exception.getClass().getSimpleName() +
                Optional.ofNullable(exception.getMessage())
                        .map(msg -> ": " + msg).orElse("");
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return EscapedCharacters.of(authorName);
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContents() {
        return EscapedCharacters.of(contents);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public OffsetDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(OffsetDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public OffsetDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(OffsetDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
