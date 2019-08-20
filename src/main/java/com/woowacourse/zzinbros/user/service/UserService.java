package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.LoginUserDto;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.EmailAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
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
            throw new EmailAlreadyExistsException("중복된 이메일이 존재합니다", e);
        }
    }

    public User modify(Long id, UserUpdateDto userUpdateDto, LoginUserDto loginUserDto) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(loginUserDto.getEmail());
        if (loggedInUser.isAuthor(user)) {
            user.update(userUpdateDto.toEntity(loggedInUser.getPassword()));
            return user;
        }
        throw new NotValidUserException("수정할 수 없는 이용자입니다");
    }

    public void delete(Long id, LoginUserDto loginUserDto) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(loginUserDto.getEmail());
        if (loggedInUser.isAuthor(user)) {
            userRepository.deleteById(id);
            return;
        }
        throw new NotValidUserException("삭제할 수 없는 이용자입니다");
    }

    public User findUserById(long id) {
        return findUser(id);
    }

    public User findLoggedInUser(final LoginUserDto loginUserDto) {
        return findUser(loginUserDto.getId());
    }

    private User findUser(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found By ID"));
    }

    public LoginUserDto login(UserRequestDto userRequestDto) {
        User user = findUserByEmail(userRequestDto.getEmail());

        if (user.matchPassword(userRequestDto.getPassword())) {
            return new LoginUserDto(user.getId(), user.getName(), user.getEmail());
        }
        throw new UserLoginException("비밀번호가 다릅니다");
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found By email"));
    }
}
