package com.woowacourse.zzinbros.common.config.upload;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class UploadToFactory {
    private final AmazonS3 amazonS3;
    @Value("${zzinbros.uploadto.strategy}")
    private String strategy;
    @Value("${zzinbros.uploadto.uploadurl}")
    private String uploadUrl;
    @Value("${zzinbros.uploadto.downloadurl}")
    private String downloadUrl;

    public UploadToFactory(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public UploadTo create(MultipartFile file) {
        if (strategy.equals("s3")) {
            return new UploadToS3(file, uploadUrl, downloadUrl, amazonS3);
        }
        if (strategy.equals("local")) {
            return new UploadToLocal(file, uploadUrl, downloadUrl);
        }
        throw new IllegalArgumentException();
    }
}
