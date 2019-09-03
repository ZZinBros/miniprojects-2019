package com.woowacourse.zzinbros.oauth.domain;

import javax.persistence.*;

@Entity
public class OauthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id", referencedColumnName = "provider_id", nullable = false, updatable = false, unique = true)
    private UserConnection social;

    private OauthUser(String email, String nickname, UserConnection social) {
        this.email = email;
        this.nickname = nickname;
        this.social = social;
    }

    protected OauthUser() {
    }

    public static OauthUser signUp(UserConnection userConnection) {
        return new OauthUser(userConnection.getEmail(),
                userConnection.getDisplayName(),
                userConnection);
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public UserConnection getSocial() {
        return social;
    }
}