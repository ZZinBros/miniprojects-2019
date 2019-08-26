package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_id", "to_id"}, name = "UK_USER_FROM_AND_TO_ID")
)
@Entity
public class Friend extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_Id", foreignKey = @ForeignKey(name = "FRIEND_TO_OTHER"), updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_Id", foreignKey = @ForeignKey(name = "FRIEND_TO_MASTER"), updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User from;

    protected Friend() {
    }

    private Friend(User from, User other) {
        this.from = from;
        this.to = other;
    }

    public static Friend of(User from, User to) {
        Friend friend = new Friend(from, to);
        return friend;
    }

    boolean isSameWithFrom(User from) {
        return this.from.equals(from);
    }

    boolean isSameWithTo(User to) {
        return this.to.equals(to);
    }

    public Long getId() {
        return id;
    }

    public User getFrom() {
        return from;
    }

    public User getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        Friend friend = (Friend) o;
        return Objects.equals(to, friend.to) &&
                Objects.equals(from, friend.from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(to, from);
    }
}
