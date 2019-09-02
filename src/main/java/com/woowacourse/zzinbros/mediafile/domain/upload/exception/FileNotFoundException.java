package com.woowacourse.zzinbros.mediafile.domain.upload.exception;

public class FileNotFoundException extends UploadException {
    public FileNotFoundException() {
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
