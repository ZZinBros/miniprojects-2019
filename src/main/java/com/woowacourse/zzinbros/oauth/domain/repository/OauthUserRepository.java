package com.woowacourse.zzinbros.oauth.domain.repository;

import com.woowacourse.zzinbros.oauth.domain.OauthUser;
import com.woowacourse.zzinbros.oauth.domain.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OauthUserRepository extends JpaRepository<OauthUser, Long> {
    Optional<OauthUser> findBySocial(UserConnection userConnection);
}
