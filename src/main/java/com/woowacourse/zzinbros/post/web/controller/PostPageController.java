package com.woowacourse.zzinbros.post.web.controller;

import com.woowacourse.zzinbros.post.domain.Post;
import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.data.domain.Sort;
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
    private final FriendService friendService;

    public PostPageController(PostService postService, UserService userService, FriendService friendService) {
        this.postService = postService;
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping(value = "posts")
    public String showPage(@RequestParam("author") final Long authorId,
                           @SessionInfo UserSession userSession,
                           Model model) {
        try {
            User author = userService.findUserById(authorId);
            Sort sort = Sort.by(Sort.Direction.DESC, "createdDateTime");
            List<Post> posts = postService.readAllByUser(author, userSession.getDto().getId(), sort);
            Set<UserResponseDto> friends = friendService.findFriendsByUserId(authorId);

            model.addAttribute("author", author);
            model.addAttribute("posts", posts);
            model.addAttribute("friends", friends);
            model.addAttribute("isMypage", userSession.matchId(authorId));
            model.addAttribute("friendStatus", friendService.isMyFriend(userSession.getDto().getId(), authorId));
            model.addAttribute("requestStatus", friendService.readyToBeMyFriend(userSession.getDto().getId(), authorId));
            return "user-page";
        } catch (UserNotFoundException e) {
            return "redirect:/";
        }
    }
}
