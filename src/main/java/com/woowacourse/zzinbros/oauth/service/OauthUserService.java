package com.woowacourse.zzinbros.oauth.service;

import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.oauth.domain.OauthUser;
import com.woowacourse.zzinbros.oauth.domain.UserConnection;
import com.woowacourse.zzinbros.oauth.domain.repository.OauthUserRepository;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OauthUserService {

    private final OauthUserRepository oauthUserRepository;
    private final UserRepository userRepository;

    public OauthUserService(OauthUserRepository oauthUserRepository, UserRepository userRepository) {
        this.oauthUserRepository = oauthUserRepository;
        this.userRepository = userRepository;
    }

    public OauthUser signUp(UserConnection userConnection) {
        final OauthUser user = OauthUser.signUp(userConnection);
        userRepository.save(new User(user.getNickname(), user.getEmail(), "12345678", new MediaFile(user.getSocial().getProfileUrl())));
        return oauthUserRepository.save(user);
    }

    public OauthUser findBySocial(UserConnection userConnection) {
        return oauthUserRepository.findBySocial(userConnection).orElseThrow(() -> new UserNotFoundException("유저 없음"));
    }

    public User oauthUserToUser(OauthUser oauthUser) {
        return userRepository.findByEmailEmail(oauthUser.getEmail()).orElseThrow(() -> new UserNotFoundException("유저 없음"));
    }

    public boolean isExistUser(UserConnection userConnection) {
        return oauthUserRepository.findBySocial(userConnection).isPresent();
    }
}
