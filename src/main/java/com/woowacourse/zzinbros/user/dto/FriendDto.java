package com.woowacourse.zzinbros.user.dto;

public class FriendDto {

    private Long requestFriendId;

    public FriendDto() {
    }

    public FriendDto(Long requestFriendId) {
        this.requestFriendId = requestFriendId;
    }

    public Long getRequestFriendId() {
        return requestFriendId;
    }

    public void setRequestFriendId(Long requestFriendId) {
        this.requestFriendId = requestFriendId;
    }
}
