package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostEditController {
    private PostService postService;

    public PostEditController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{id}/edit")
    public ModelAndView showEdit(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("post-modify");
        modelAndView.addObject("post", postService.read(id));
        return modelAndView;
    }
}
