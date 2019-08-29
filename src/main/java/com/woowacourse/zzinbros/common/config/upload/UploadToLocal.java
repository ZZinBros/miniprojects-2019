package com.woowacourse.zzinbros.common.config.upload;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

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
        if (file == null) {
            return null;
        }
        try {
            String saveName = hashFileName() + getExtension();
            file.transferTo(new File(uploadUrl + saveName));
            log.debug("FILE SAVED : {}", uploadUrl + saveName);
            return downloadUrl + saveName;
        } catch (IOException e) {
            log.warn("IOException " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }
}