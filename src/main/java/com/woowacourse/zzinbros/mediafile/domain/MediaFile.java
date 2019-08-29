package com.woowacourse.zzinbros.mediafile.domain;

import com.woowacourse.zzinbros.common.domain.BaseEntity;

import javax.persistence.Entity;

@Entity
public class MediaFile extends BaseEntity {
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
