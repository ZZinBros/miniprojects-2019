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

import java.util.Set;

@Controller
@RequestMapping("/friends")
public class FriendController {
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Set<UserResponseDto>> getFriends(@SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        Set<UserResponseDto> friends = friendService.findFriendsByUser(loginUserDto);
        return new ResponseEntity<>(friends, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.FOUND)
    public String requestFriend(@RequestBody FriendRequestDto friendRequestDto,
                                @SessionInfo UserSession userSession) {
        UserResponseDto loginUserDto = userSession.getDto();
        if (!userSession.matchId(friendRequestDto.getRequestFriendId())) {
            friendService.registerFriend(loginUserDto, friendRequestDto);
        }
        return "redirect:/posts?author=" + friendRequestDto.getRequestFriendId();
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<UserResponseDto> deleteFriend(@SessionInfo UserSession userSession,
                                                        @PathVariable("id") long id) {
        UserResponseDto loginUserDto = userSession.getDto();
        friendService.deleteFriends(loginUserDto, id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
