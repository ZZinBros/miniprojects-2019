package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.common.config.upload.UploadTo;
import com.woowacourse.zzinbros.mediafile.domain.MediaFile;
import com.woowacourse.zzinbros.mediafile.service.MediaFileService;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.dto.UserUpdateDto;
import com.woowacourse.zzinbros.user.exception.EmailAlreadyExistsException;
import com.woowacourse.zzinbros.user.exception.NotValidUserException;
import com.woowacourse.zzinbros.user.exception.UserLoginException;
import com.woowacourse.zzinbros.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final MediaFileService mediaFileService;

    public UserService(UserRepository userRepository, MediaFileService mediaFileService) {
        this.userRepository = userRepository;
        this.mediaFileService = mediaFileService;
    }

    public User register(UserRequestDto userRequestDto, UploadTo uploadTo) {
        final String email = userRequestDto.getEmail();
        if (!userRepository.existsUserByEmail(email)) {
            MediaFile mediaFile = mediaFileService.register(uploadTo);
            return userRepository.save(userRequestDto.toEntity(mediaFile));
        }
        throw new EmailAlreadyExistsException("중복된 이메일이 존재합니다");
    }

    public User modify(long id,
                       UserUpdateDto userUpdateDto,
                       UserResponseDto loginUserDto,
                       UploadTo uploadTo) {
        User user = findUser(id);
        User loggedInUser = findUserByEmail(loginUserDto.getEmail());
        if (loggedInUser.isAuthor(user)) {
            MediaFile mediaFile = mediaFileService.register(uploadTo);
            user.update(userUpdateDto.toEntity(loggedInUser.getPassword(), mediaFile));
            return user;
        }
        throw new NotValidUserException("수정할 수 없는 이용자입니다");
    }

    public void delete(Long id, UserResponseDto loginUserDto) {
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

    public User findLoggedInUser(final UserResponseDto loginUserDto) {
        return findUser(loginUserDto.getId());
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found By ID"));
    }

    public UserResponseDto login(UserRequestDto userRequestDto) {
        User user = findUserByEmail(userRequestDto.getEmail());

        if (user.matchPassword(userRequestDto.getPassword())) {
            return new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getProfile().getUrl());
        }
        throw new UserLoginException("비밀번호가 다릅니다");
    }

    public List<UserResponseDto> readAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getEmail(), user.getProfile().getUrl()))
                .collect(Collectors.toList());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found By email"));
    }
}
