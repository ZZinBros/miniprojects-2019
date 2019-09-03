package com.woowacourse.zzinbros.common.web.controller;

import com.woowacourse.zzinbros.post.service.PostService;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.by;

@Controller
public class MainController {
    private static final int USER_SHOW_COUNT = 8;

    private final PostService postService;
    private final UserService userService;

    public MainController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String demo(Model model, @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        Sort sort = by(Direction.DESC, "createdDateTime");
        model.addAttribute("posts", postService.readAll(loginUserDto.getId(), sort));
        model.addAttribute("users",
                userService.convertToUserResponseDto(userService.findRandomUsers(USER_SHOW_COUNT)));
        return "index";
    }

    @GetMapping("/entrance")
    public String enter() {
        return "entrance";
    }
}
