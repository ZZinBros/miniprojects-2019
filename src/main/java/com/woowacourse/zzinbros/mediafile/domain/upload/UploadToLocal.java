package com.woowacourse.zzinbros.mediafile.domain.upload;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class UploadToLocal extends AbstractUploadTo {
    private String uploadUrl;
    private String downloadUrl;

    public UploadToLocal(MultipartFile file,
                         @NotNull String uploadUrl,
                         @NotNull String downloadUrl) {
        super(file);
        this.uploadUrl = new File("").getAbsolutePath() + uploadUrl;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String save() {
        if (Objects.isNull(file)) {
            return null;
        }
        try {
            String saveName = hashFileName() + getExtension();
            file.transferTo(new File(uploadUrl + saveName));
            LOGGER.debug("FILE SAVED : {}", uploadUrl + saveName);
            return null;
        } catch (IOException e) {
            LOGGER.warn("IOException " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}