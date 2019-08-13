package com.woowacourse.zzinbros.user;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(UserRequestDto userRequestDto) {
        return userRepository.save(userRequestDto.toEntity());
    }
}
