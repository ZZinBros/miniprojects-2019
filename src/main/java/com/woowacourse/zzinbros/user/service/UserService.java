package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.UserRepository;
import com.woowacourse.zzinbros.user.domain.UserSession;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(UserRequestDto userRequestDto) {
        try {
            return userRepository.save(userRequestDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException();
        }
    }

    public User modify(Long id, UserRequestDto userRequestDto, UserSession userSession) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(userSession.getEmail());
        if (loggedInUser.isAuthor(user)) {
            user.update(userRequestDto.toEntity());
            return user;
        }
        throw new NotValidUserException("수정할 수 없는 이용자입니다");
    }

    public void delete(Long id, UserSession userSession) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(userSession.getEmail());
        if (loggedInUser.isAuthor(user)) {
            userRepository.deleteById(id);
            return;
        }
        throw new NotValidUserException("삭제할 수 없는 이용자입니다");
    }

    public User findUserById(long id) {
        return findUser(id);
    }

    private User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserSession login(UserRequestDto userRequestDto) {
        User user = findUserByEmail(userRequestDto.getEmail());
        User requestUser = userRequestDto.toEntity();

        if (user.isAuthor(requestUser)) {
            return new UserSession(userRequestDto.getName(), userRequestDto.getEmail());
        }
        throw new UserLoginException();
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
