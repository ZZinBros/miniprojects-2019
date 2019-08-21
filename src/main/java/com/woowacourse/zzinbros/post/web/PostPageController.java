package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostPageController {

    private final PostService postService;
    private final UserService userService;

    public PostPageController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(value = "posts")
    public String showPage(@RequestParam("author") final Long id, Model model) {
        try {
            User author = userService.findUserById(id);
            List<Post> posts = postService.readAllByUser(author);
            model.addAttribute("author", author);
            model.addAttribute("posts", posts);
            return "user-page";
        } catch (UserNotFoundException e) {
            return "redirect:/";
        }
    }
}
