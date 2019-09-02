package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"sender_id", "receiver_id"},
        name = "uk_friend_request_sender_and_receiver"))
public class FriendRequest extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "sender_id",
            foreignKey = @ForeignKey(name = "fk_friend_request_sender_to_user"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id",
            foreignKey = @ForeignKey(name = "fk_friend_request_receiver_to_user"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;

    protected FriendRequest() {
    }

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
}
