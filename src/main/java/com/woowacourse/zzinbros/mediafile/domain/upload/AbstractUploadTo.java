package com.woowacourse.zzinbros.mediafile.domain.upload;

import com.woowacourse.zzinbros.mediafile.domain.upload.exception.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractUploadTo implements UploadTo {
    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractUploadTo.class);
    private static final int LAST_INDEX = -1;
    private static final String EXTENSION_SPLITTER = ".";

    protected MultipartFile file;

    protected AbstractUploadTo(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String getExtension() {
        String originName = file.getOriginalFilename();
        if (Objects.isNull(originName)) {
            throw new FileNotFoundException();
        }
        int lastIndex = originName.lastIndexOf(EXTENSION_SPLITTER);
        if (lastIndex == LAST_INDEX) {
            LOGGER.warn("확장자가 없는 파일이네여");
            return "";
        }
        return originName.substring(lastIndex);
    }

    @Override
    public abstract String save();

    protected String hashFileName() {
        String fileName = file.getOriginalFilename();
        if (Objects.isNull(fileName)) {
            throw new FileNotFoundException();
        }
        return UUID.randomUUID().toString();
    }
}
