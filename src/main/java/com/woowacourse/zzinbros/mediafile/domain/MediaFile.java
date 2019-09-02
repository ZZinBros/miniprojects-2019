package com.woowacourse.zzinbros.mediafile.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@Entity
public class MediaFile extends BaseEntity {
    @Embedded
    @Column(name = "url", nullable = false)
    private ImageUrl url;

    protected MediaFile() {
    }

    public MediaFile(String url) {
        this.url = new ImageUrl(url);
    }

    public String getUrl() {
        return url.getUrl();
    }
}
