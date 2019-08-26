package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/friends")
public class FriendController {

    private UserService userService;
    private FriendService friendService;

    public FriendController(UserService userService, FriendService friendService) {
        this.userService = userService;
        this.friendService = friendService;
    }

    @GetMapping("/requests")
    public ResponseEntity<Set<UserResponseDto>> showFriendRequests(@SessionInfo UserSession session) {
        Set<UserResponseDto> users = friendService.findFriendRequestsByUser(session.getDto());
        return new ResponseEntity<>(users, HttpStatus.OK);
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
