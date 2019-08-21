package com.woowacourse.zzinbros.comment.dto;

public class CommentRequestDto {
    private Long postId;
    private Long commentId;
    private String contents;

    public CommentRequestDto() {
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(final Long postId) {
        this.postId = postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(final Long commentId) {
        this.commentId = commentId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
