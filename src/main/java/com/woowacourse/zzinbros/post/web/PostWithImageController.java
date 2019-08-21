package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.mediafile.web.support.UploadTo;
import com.woowacourse.zzinbros.mediafile.web.support.UploadedFile;
import com.woowacourse.zzinbros.post.dto.PostRequestDto;
import com.woowacourse.zzinbros.post.service.PostWithImageService;
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
                         @UploadedFile UploadTo uploadToLocal) {
        postService.add(postRequestDto, 999L, uploadToLocal);
        return "redirect:/";
    }
}
