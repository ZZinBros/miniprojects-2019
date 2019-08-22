package com.woowacourse.zzinbros.user.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_id", "to_id"})
)
@Entity
public class Friend implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "to_Id", foreignKey = @ForeignKey(name = "FRIEND_TO_OTHER"), updatable = false, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User to;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
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
//        from.addFriend(friend);
        return friend;
    }
//
//    public boolean isFollowTo(TempUser other) {
//        return to.equals(other);
//    }

    public boolean isSameWithFrom(User from) {
        return this.from.equals(from);
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
