package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.EmailAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import com.woowacourse.zzinbros.user.web.support.UserSession;
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
            throw new EmailAlreadyExistsException("중복된 이메일이 존재합니다", e);
        }
    }

    public User modify(Long id, UserUpdateDto userUpdateDto, UserSession userSession) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(userSession.getEmail());
        if (loggedInUser.isAuthor(user)) {
            user.update(userUpdateDto.toEntity(loggedInUser.getPassword()));
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
                .orElseThrow(() -> new UserNotFoundException("User Not Found By ID"));
    }

    public UserSession login(UserRequestDto userRequestDto) {
        User user = findUserByEmail(userRequestDto.getEmail());

        if (user.matchPassword(userRequestDto.getPassword())) {
            return new UserSession(user.getId(), user.getName(), user.getEmail());
        }
        throw new UserLoginException("비밀번호가 다릅니다");
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found By email"));
    }
}
