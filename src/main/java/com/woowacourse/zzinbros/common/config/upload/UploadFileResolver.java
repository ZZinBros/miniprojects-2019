package com.woowacourse.zzinbros.common.config.upload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Component
public class UploadFileResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UploadToFactory uploadToFactory;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UploadedFile.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) webRequest.getNativeRequest();
        MultipartFile multipartFile = request.getFile("feed-image");

        return uploadToFactory.create(multipartFile);
    }
}