package com.woowacourse.zzinbros.oauth.domain;

import com.woowacourse.zzinbros.oauth.security.ProviderType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class UserConnection implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column(name = "provider_id", unique = true, nullable = false)
    private String providerId;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "profile_url")
    private String profileUrl;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expire_time")
    private long expireTime;

    protected UserConnection() {
    }

    private UserConnection(String email, ProviderType provider,
                           String providerId, String displayName,
                           String profileUrl, String accessToken,
                           long expireTime) {

        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }

    public static UserConnection valueOf(FacebookUserDetails userDetails) {
        return new UserConnection(userDetails.getEmail(),
                ProviderType.FACEBOOK, userDetails.getId(),
                userDetails.getName(),
                userDetails.getImageUrl(),
                userDetails.getAccess_token(),
                userDetails.getExpiration());
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public ProviderType getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }
}