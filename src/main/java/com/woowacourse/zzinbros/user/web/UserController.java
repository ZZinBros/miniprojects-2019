package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.UserDuplicatedException;
import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> show(@PathVariable("id") Long id) {
        try {
            User user = userService.findUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<User> register(@RequestBody UserRequestDto userRequestDto) {
        try {
            User user = userService.register(userRequestDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserDuplicatedException e) {
            return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id, @RequestBody UserRequestDto userRequestDto, UserSession userSession) {
        try {
            User user = userService.modify(id, userRequestDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id, UserSession userSession, HttpSession session) {
        userService.resign(id);
        session.removeAttribute(UserSession.LOGIN_USER);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
