package com.woowacourse.zzinbros.user.dto;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().isAssignableFrom(FriendRequestDto.class))) return false;
        FriendRequestDto friendRequestDto = (FriendRequestDto) o;
        return Objects.equals(requestFriendId, friendRequestDto.requestFriendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestFriendId);
    }
}
