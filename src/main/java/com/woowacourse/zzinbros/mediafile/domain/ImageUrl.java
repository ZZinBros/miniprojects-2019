package com.woowacourse.zzinbros.mediafile.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ImageUrl {
    private static final String DEFAULT_URL = "/images/default/eastjun_profile.jpg";

    private String url;

    private ImageUrl() {
    }

    public ImageUrl(String url) {
        this.url = validate(url);
    }

    private String validate(String url) {
        if (Objects.isNull(url)) {
            return DEFAULT_URL;
        }
        return url;
    }

    public String getUrl() {
        return url;
    }
}
