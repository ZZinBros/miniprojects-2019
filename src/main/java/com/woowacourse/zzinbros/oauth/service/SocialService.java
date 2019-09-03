package com.woowacourse.zzinbros.oauth.service;

import com.woowacourse.zzinbros.oauth.domain.OauthUser;
import com.woowacourse.zzinbros.oauth.domain.UserConnection;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.UserService;
import com.woowacourse.zzinbros.user.web.support.LoginSessionManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SocialService {
    private final OauthUserService oauthUserService;
    private final LoginSessionManager loginSessionManager;
    private final UserService userService;

    public SocialService(OauthUserService oauthUserService, LoginSessionManager loginSessionManager, UserService userService) {
        this.oauthUserService = oauthUserService;
        this.loginSessionManager = loginSessionManager;
        this.userService = userService;
    }

    public UsernamePasswordAuthenticationToken doAuthentication(UserConnection userConnection) {

        if (oauthUserService.isExistUser(userConnection)) {
            final OauthUser oauthUser = oauthUserService.findBySocial(userConnection);
            User user = oauthUserService.oauthUserToUser(oauthUser);
            loginSessionManager.setLoginSession(toResponseDto(user));
            return setAuthenticationToken(oauthUser);
        }

        final OauthUser oauthUser = oauthUserService.signUp(userConnection);
        User user = oauthUserService.oauthUserToUser(oauthUser);
        loginSessionManager.setLoginSession(toResponseDto(user));
        return setAuthenticationToken(oauthUser);

    }

    private UsernamePasswordAuthenticationToken setAuthenticationToken(Object user) {
        return new UsernamePasswordAuthenticationToken(user, null, getAuthorities("ROLE_USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getProfile().getUrl());
    }
}
