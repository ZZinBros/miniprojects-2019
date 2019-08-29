package com.woowacourse.zzinbros.comment.dto;

import com.woowacourse.zzinbros.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.Optional;

public class CommentResponseDto {
    private Long commentId;
    private Long authorId;
    private String authorName;
    private String contents;
    private String errorMessage;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
    private String profile;

    public CommentResponseDto() {
    }

    public CommentResponseDto(final Comment comment) {
        this.commentId = comment.getId();
        this.authorId = comment.getAuthor().getId();
        this.authorName = comment.getAuthor().getName();
        this.contents = comment.getContents();
        this.createdDateTime = comment.getCreatedDateTime();
        this.updatedDateTime = comment.getUpdatedDateTime();
        this.profile = comment.getAuthor().getProfile().getUrl();
    }

    public CommentResponseDto(final Exception exception) {
        this.errorMessage = exception.getClass().getSimpleName() +
                Optional.ofNullable(exception.getMessage())
                        .map(msg -> ": " + msg).orElse("");
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(final Long commentId) {
        this.commentId = commentId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(final Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(final String authorName) {
        this.authorName = authorName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(final LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    public void setUpdatedDateTime(final LocalDateTime updatedDateTime) {
        this.updatedDateTime = updatedDateTime;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
