package com.woowacourse.zzinbros.user.web.support;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.UserNotLoggedInException;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SessionInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = httpServletRequest.getSession();
        Optional<UserResponseDto> loginUserDto
                = Optional.ofNullable((UserResponseDto) session.getAttribute(UserSession.LOGIN_USER));

        return new UserSession(loginUserDto.orElseThrow(() -> new UserNotLoggedInException("로그인 해주세요")));
    }
}
