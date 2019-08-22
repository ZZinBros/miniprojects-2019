package com.woowacourse.zzinbros.common.config.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadToFactory {
    @Value("${zzinbros.uploadto.strategy}")
    private String strategy;
    @Value("${zzinbros.uploadto.uploadurl}")
    private String uploadUrl;
    @Value("${zzinbros.uploadto.downloadurl}")
    private String downloadUrl;

    public UploadToFactory() {
    }

    public UploadTo create(MultipartFile file) {
        if (strategy.equals("s3")) {
            return new UploadToS3(file, uploadUrl, downloadUrl);
        }
        if (strategy.equals("local")) {
            return new UploadToLocal(file, uploadUrl, downloadUrl);
        }
        throw new IllegalArgumentException();
    }
}
