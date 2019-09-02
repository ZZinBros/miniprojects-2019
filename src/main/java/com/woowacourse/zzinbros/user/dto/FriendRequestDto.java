package com.woowacourse.zzinbros.user.dto;

public class FriendRequestDto {
    private Long requestFriendId;

    public FriendRequestDto() {
    }

    public FriendRequestDto(Long requestFriendId) {
        this.requestFriendId = requestFriendId;
    }

    public Long getRequestFriendId() {
        return requestFriendId;
    }

    public void setRequestFriendId(Long requestFriendId) {
        this.requestFriendId = requestFriendId;
    }
}
