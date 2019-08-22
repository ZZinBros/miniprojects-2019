package com.woowacourse.zzinbros.common;

import com.woowacourse.zzinbros.mediafile.web.support.UploadFileResolver;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
        resolvers.add(new UploadFileResolver());
    }
}
