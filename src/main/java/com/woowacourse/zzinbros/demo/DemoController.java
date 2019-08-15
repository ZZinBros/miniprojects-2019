package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.post.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {
    private PostService postService;

    public DemoController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String demo(Model model) {
        model.addAttribute("posts", postService.readAll());
        return "index";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
