package com.woowacourse.zzinbros.user.web.controller.rest;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.FriendService;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@Controller
public class FriendRequestController {

    private FriendService friendService;

    public FriendRequestController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/friends-requests")
    public ResponseEntity<Set<UserResponseDto>> getFriendRequests(@SessionInfo UserSession userSession) {
        Set<UserResponseDto> requests = friendService.findFriendRequestsByUser(userSession.getDto());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @DeleteMapping("/friends-requests/{id}")
    public ResponseEntity<UserResponseDto> deleteFriendRequest(@PathVariable("id") long id,
                                                               @SessionInfo UserSession userSession) {
        UserResponseDto userResponseDto = userSession.getDto();
        friendService.deleteFriendRequest(userResponseDto, id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
