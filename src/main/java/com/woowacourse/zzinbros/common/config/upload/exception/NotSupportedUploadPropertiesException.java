package com.woowacourse.zzinbros.common.config.upload.exception;

public class NotSupportedUploadPropertiesException extends UploadException {
    public NotSupportedUploadPropertiesException() {
        super("zzinbros.uploadto. 설정이 필요합니다");
    }
}
