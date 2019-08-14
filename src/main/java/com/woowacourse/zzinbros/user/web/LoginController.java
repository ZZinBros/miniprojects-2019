package com.woowacourse.zzinbros.user.web;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public void login(@RequestBody UserRequestDto userRequestDto) {

    }
}
