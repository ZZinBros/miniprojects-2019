package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.post.service.PostService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.data.domain.Sort.*;

@Controller
public class DemoController {
    private PostService postService;

    public DemoController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String demo(Model model) {
        Sort sort = by(Direction.DESC, "createdDateTime");
        model.addAttribute("posts", postService.readAll(sort));
        return "index";
    }

    @GetMapping("/entrance")
    public String enter() {
        return "entrance";
    }
}
