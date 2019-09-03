package com.woowacourse.zzinbros.oauth.domain;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class FacebookUserDetails {

    private Picture picture;
    private String id;
    private String name;
    private String email;
    private long expiration;
    private String access_token;

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.access_token = accessToken.getValue();
        this.expiration = accessToken.getExpiration().getTime();
    }

    public String getImageUrl() {
        return picture.getData().getUrl();
    }

    public Picture getPicture() {
        return picture;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getExpiration() {
        return expiration;
    }

    public String getAccess_token() {
        return access_token;
    }

    public static class Picture {
        private Data data;

        public Data getData() {
            return data;
        }

        public static class Data {
            private int height;
            private int width;
            private boolean isSilhouette;
            private String url;

            public String getUrl() {
                return url;
            }
        }
    }
}