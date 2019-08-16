package com.woowacourse.zzinbros.comment.dto;

public class CommentResponseDto {
    private String author;
    private String contents;

    public CommentResponseDto() {
    }

    public CommentResponseDto(String author, String contents) {
        this.author = author;
        this.contents = contents;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
