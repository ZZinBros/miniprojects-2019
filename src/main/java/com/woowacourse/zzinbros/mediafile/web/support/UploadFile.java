package com.woowacourse.zzinbros.mediafile.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class UploadFile {
    private static final Logger logger = LoggerFactory.getLogger(UploadFile.class);
    private static final String PATH = "/Users/jyk/Desktop/WooWa/miniprojects-2019/src/main/resources/static/images/";
    private MultipartFile file;

    public UploadFile() {
    }

    public UploadFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    private String getExtension() {
        String originName = file.getOriginalFilename();
        if (originName == null) {
            throw new IllegalArgumentException();
        }
        int lastIndexOf = originName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            logger.warn("확장자가 없는 파일이네여");
            return "";
        }
        return originName.substring(lastIndexOf);
    }


    public String save() {
        try {
            String saveName = hashFileName() + getExtension();
            file.transferTo(new File(PATH + saveName));
            logger.debug("FILE SAVED : {}", PATH + saveName);
            return "/images/" + saveName;
        } catch (IOException e) {
            logger.warn("IOException " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

    private String hashFileName() {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException();
        }
        return UUID.randomUUID().toString();
    }
}