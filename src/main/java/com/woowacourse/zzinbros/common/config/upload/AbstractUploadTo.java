package com.woowacourse.zzinbros.common.config.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public abstract class AbstractUploadTo implements UploadTo {
    protected static final Logger log = LoggerFactory.getLogger(AbstractUploadTo.class);
    protected MultipartFile file;

    protected AbstractUploadTo(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String getExtension() {
        String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new IllegalArgumentException();
        }
        int lastIndexOf = originName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            log.warn("확장자가 없는 파일이네여");
            return "";
        }
        return originName.substring(lastIndexOf);
    }

    @Override
    public abstract String save();

    protected String hashFileName() {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        return UUID.randomUUID().toString();
    }
}
