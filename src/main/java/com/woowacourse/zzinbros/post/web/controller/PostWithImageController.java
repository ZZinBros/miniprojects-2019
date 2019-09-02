package com.woowacourse.zzinbros.post.web.controller;

import com.woowacourse.zzinbros.mediafile.domain.upload.UploadTo;
import com.woowacourse.zzinbros.mediafile.domain.upload.support.UploadedFile;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostWithImageService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts-with-images")
public class PostWithImageController {
    private final PostWithImageService postService;

    public PostWithImageController(PostWithImageService postService) {
        this.postService = postService;
    }

    @PostMapping
    public String upload(PostRequestDto postRequestDto,
                         @UploadedFile UploadTo uploadTo,
                         @SessionInfo UserSession userSession) {
        postService.add(postRequestDto, userSession.getDto().getId(), uploadTo);
        return "redirect:/";
    }
}
