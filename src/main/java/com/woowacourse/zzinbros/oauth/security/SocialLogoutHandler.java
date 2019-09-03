package com.woowacourse.zzinbros.oauth.security;

import com.woowacourse.zzinbros.oauth.domain.OauthUser;
import com.woowacourse.zzinbros.oauth.domain.UserConnection;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SocialLogoutHandler implements LogoutHandler {
    private static final String GRAPH_URL = "https://graph.facebook.com/";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserConnection authDetail = ((OauthUser) authentication.getPrincipal()).getSocial();
        String providerId = authDetail.getProviderId();
        String accessToken = authDetail.getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(GRAPH_URL + providerId + "/permissions?access_token=" + accessToken);
    }
}
