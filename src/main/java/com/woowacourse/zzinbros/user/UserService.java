package com.woowacourse.zzinbros.user;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(UserRequestDto userRequestDto) {
        return userRepository.save(userRequestDto.toEntity());
    }
}
