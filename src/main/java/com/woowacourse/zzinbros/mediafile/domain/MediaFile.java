package com.woowacourse.zzinbros.mediafile.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MediaFile extends BaseEntity {
    @Column(name = "url", nullable = false)
    private String url;

    protected MediaFile() {
    }

    public MediaFile(String url) {
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
