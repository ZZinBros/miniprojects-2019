package com.woowacourse.zzinbros.user.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"from_id", "to_id"})
)
@Entity
public class Friend implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
