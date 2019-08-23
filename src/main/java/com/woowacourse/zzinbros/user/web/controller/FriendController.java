package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
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
    private FriendService friendService;

    public FriendController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    public String addFriend(@RequestBody FriendRequestDto friendRequestDto, @SessionInfo UserSession userSession) {
        final UserResponseDto loginUserDto = userSession.getDto();
        if (!userSession.matchId(friendRequestDto.getRequestFriendId())) {
            friendService.sendFriendRequest(loginUserDto, friendRequestDto);
        }
        return "redirect:/posts?author=" + friendRequestDto.getRequestFriendId();
    }
}
