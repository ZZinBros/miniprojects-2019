package com.woowacourse.zzinbros.user.domain.repository;

import com.woowacourse.zzinbros.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmailEmail(String email);

    Optional<User> findByEmailEmail(String email);

    List<User> findByNameNameContaining(String name, Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.createdDateTime DESC")
    List<User> findLatestUsers(Pageable pageable);
}
