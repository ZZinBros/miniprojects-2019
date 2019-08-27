package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"sender_id", "receiver_id"},
                name = "UK_USER_SENDER_AND_RECEIVER")
)
@Entity
public class Friend extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_Id",
            foreignKey = @ForeignKey(name = "FRIEND_TO_SENDER"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_Id",
            foreignKey = @ForeignKey(name = "FRIEND_TO_RECEIVER"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User receiver;


    protected Friend() {
    }

    public Friend(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public static Friend of(User user, User another) {
        Friend friend = new Friend(user, another);
        user.getFollowing().add(friend);
        another.getFollowedBy().add(friend);
        return friend;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
}
