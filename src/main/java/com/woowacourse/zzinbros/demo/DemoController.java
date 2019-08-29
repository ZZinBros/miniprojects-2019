package com.woowacourse.zzinbros.demo;

import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.data.domain.Sort.*;

@Controller
public class DemoController {
    private PostService postService;
    private UserService userService;

    public DemoController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String demo(Model model, @SessionInfo UserSession userSession) {
        Sort sort = by(Direction.DESC, "createdDateTime");
        model.addAttribute("posts", postService.readAll(sort));
        model.addAttribute("users", userService.readAll());
        return "index";
    }

    @GetMapping("/entrance")
    public String enter() {
        return "entrance";
    }
}
