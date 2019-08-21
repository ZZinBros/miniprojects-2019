package com.woowacourse.zzinbros.post.web;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

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
            Set<UserResponseDto> friends = userService.getFriendsOf(id);
            model.addAttribute("author", author);
            model.addAttribute("posts", posts);
            model.addAttribute("friends", friends);
            return "user-page";
        } catch (UserNotFoundException e) {
            return "redirect:/";
        }
    }
}
