package com.woowacourse.zzinbros.comment.dto;

public class CommentRequestDto {
    private Long postId;
    private String contents;

    public CommentRequestDto(final Long postId, final String contents) {
        this.postId = postId;
        this.contents = contents;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(final Long postId) {
        this.postId = postId;
    }

    public void setPostId(final String postId) {
        this.postId = Long.parseLong(postId);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
