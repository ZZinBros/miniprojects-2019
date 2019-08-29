package com.woowacourse.zzinbros.common.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.woowacourse.zzinbros.common.config.upload.UploadFileResolver;
import com.woowacourse.zzinbros.user.web.support.UserArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@SpringBootConfiguration
public class AppConfig implements WebMvcConfigurer {
    @Autowired
    private UploadFileResolver uploadFileResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserArgumentResolver());
        resolvers.add(uploadFileResolver);
    }

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3Client.builder().standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
