package com.woowacourse.zzinbros.oauth.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/oauth")
public class OauthController {

    @GetMapping("/login")
    public Principal oauth(Principal principal) {
        return principal;
    }
}
