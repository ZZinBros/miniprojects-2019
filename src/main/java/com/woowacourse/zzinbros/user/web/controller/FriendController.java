package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friends")
public class FriendController {

    private FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    public String requestFriend(@RequestBody FriendRequestDto friendRequestDto, @SessionInfo UserSession userSession) {
        final UserResponseDto loginUserDto = userSession.getDto();
        if (!userSession.matchId(friendRequestDto.getRequestFriendId())) {
            friendService.registerFriend(loginUserDto, friendRequestDto);
        }
        return "redirect:/posts?author=" + friendRequestDto.getRequestFriendId();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserResponseDto> requestFriend(
            @SessionInfo UserSession userSession,
            @PathVariable("id") long id) {
        final UserResponseDto loginUserDto = userSession.getDto();
        friendService.deleteFriends(loginUserDto, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
