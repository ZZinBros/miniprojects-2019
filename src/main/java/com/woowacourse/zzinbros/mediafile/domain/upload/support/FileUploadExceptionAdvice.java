package com.woowacourse.zzinbros.mediafile.domain.upload.support;

import org.apache.tomcat.util.http.fileupload.FileUploadBase;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler({FileUploadBase.FileSizeLimitExceededException.class, MaxUploadSizeExceededException.class})
    public String handleFileUploadException(Exception e, RedirectAttributes redirectAttr) {
        redirectAttr.addFlashAttribute("errorMessage", "파일 크기가 너무 큽니다.");
        return "redirect:/entrance";
    }
}
