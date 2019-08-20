package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user-page")
public class UserPageController {
    private UserService userService;
    private PostService postService;

    public UserPageController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String showPage(@PathVariable("id") final Long id, Model model) {
        try {
            User author = userService.findUserById(id);
            List<Post> posts = postService.readAllByUser(author);
            model.addAttribute("posts", posts);
            return "/user-page";
        } catch (UserNotFoundException e) {
            return "redirect:/";
        }
    }
}
