package com.woowacourse.zzinbros.mediafile.domain.upload;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class UploadToS3 extends AbstractUploadTo {
    private AmazonS3 amazonS3;
    private String bucketName;
    private String bucketUrl;

    public UploadToS3(MultipartFile file,
                      @NotNull String bucketName,
                      @NotNull String bucketUrl,
                      AmazonS3 amazonS3) {
        super(file);
        this.bucketName = bucketName;
        this.bucketUrl = bucketUrl;
        this.amazonS3 = amazonS3;
    }

    @Override
    public String save() {
        if (Objects.isNull(file) || file.isEmpty()) {
            return null;
        }
        String keyName = hashFileName() + getExtension();
        File localFile = convertMultiPartToFile(file);
        amazonS3.putObject(bucketName, keyName, localFile);
        localFile.delete();
        return bucketUrl + keyName;
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convertFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertFile)) {
            fos.write(file.getBytes());
            fos.close();
            return convertFile;
        } catch (IOException e) {
            LOGGER.warn("FILE CONVERT FAILED : {}", e.getMessage());
            throw new IllegalAccessError();
        }
    }
}
