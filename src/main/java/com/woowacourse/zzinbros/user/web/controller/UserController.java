package com.woowacourse.zzinbros.user.web.controller;

import com.woowacourse.zzinbros.common.config.upload.UploadTo;
import com.woowacourse.zzinbros.common.config.upload.UploadedFile;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.UserException;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.exception.UserRegisterException;
import com.woowacourse.zzinbros.user.web.support.LoginSessionManager;
import com.woowacourse.zzinbros.user.web.support.SessionInfo;
import com.woowacourse.zzinbros.user.web.support.UserSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final LoginSessionManager loginSessionManager;

    public UserController(UserService userService, LoginSessionManager loginSessionManager) {
        this.userService = userService;
        this.loginSessionManager = loginSessionManager;
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
    public String register(UserRequestDto userRequestDto,
                           @UploadedFile UploadTo uploadTo) {
        try {
            userService.register(userRequestDto, uploadTo);
            return "redirect:/entrance";
        } catch (UserException e) {
            throw new UserRegisterException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public String modify(@PathVariable Long id,
                         UserUpdateDto userUpdateDto,
                         @SessionInfo UserSession userSession,
                         @UploadedFile UploadTo uploadTo) {
        try {
            User user = userService.modify(id, userUpdateDto, userSession.getDto(), uploadTo);
            UserResponseDto newLoginUserDto = new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getProfile().getUrl());
            loginSessionManager.setLoginSession(newLoginUserDto);
            return "redirect:/";
        } catch (UserException e) {
            return "redirect:/users/" + id;
        }
    }

    @DeleteMapping("/{id}")
    public String resign(@PathVariable Long id, @SessionInfo UserSession userSession) {
        userService.delete(id, userSession.getDto());
        loginSessionManager.clearSession();
        return "redirect:/";
    }
}
