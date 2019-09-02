package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        columnNames = {"owner_id", "slave_id"},
        name = "UK_USER_SENDER_AND_RECEIVER"))
public class Friend extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_Id",
            foreignKey = @ForeignKey(name = "FRIEND_TO_SENDER"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slave_Id",
            foreignKey = @ForeignKey(name = "FRIEND_TO_RECEIVER"),
            updatable = false,
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User slave;

    protected Friend() {
    }

    public Friend(User owner, User slave) {
        this.owner = owner;
        this.slave = slave;
    }

    public User getOwner() {
        return owner;
    }

    public User getSlave() {
        return slave;
    }
}
