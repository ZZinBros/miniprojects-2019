package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.UserDuplicatedException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
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
        if (!userRepository.existsUserByEmail(userRequestDto.getEmail())) {
            return userRepository.save(userRequestDto.toEntity());
        }
        throw new UserDuplicatedException();
    }

    public User update(Long id, UserRequestDto userRequestDto) {
        User user = findUser(id);
        user.update(userRequestDto.toEntity());
        return user;
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public User findUserById(long id) {
        return findUser(id);
    }

    private User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
