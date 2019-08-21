package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.dto.FriendDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/friends")
public class FriendController {

    private UserService userService;

    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    public String addFriend(@RequestBody FriendDto friendDto, @SessionInfo UserSession userSession) {
        final Long loginUserId = userSession.getDto().getId();
        userService.addFriends(friendDto.getRequestFriendId(), loginUserId);
        userService.addFriends(loginUserId, friendDto.getRequestFriendId());
        return "redirect:/posts?author=" + friendDto.getRequestFriendId();
    }

}
