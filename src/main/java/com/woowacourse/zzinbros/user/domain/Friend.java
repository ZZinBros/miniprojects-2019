package com.woowacourse.zzinbros.user.domain;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
    private TempUser to;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "from_Id", foreignKey = @ForeignKey(name = "FRIEND_TO_MASTER"), updatable = false, nullable = false)
    private TempUser from;

    protected Friend() {
    }

    private Friend(TempUser from, TempUser other) {
        this.from = from;
        this.to = other;
    }

    public static Friend of(TempUser from, TempUser to) {
        Friend friend = new Friend(from, to);
//        from.addFriend(friend);
        return friend;
    }
//
//    public boolean isFollowTo(TempUser other) {
//        return to.equals(other);
//    }

    public Long getId() {
        return id;
    }

    public TempUser getFrom() {
        return from;
    }

    public TempUser getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend)) return false;
        Friend friend = (Friend) o;
        return Objects.equals(id, friend.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
