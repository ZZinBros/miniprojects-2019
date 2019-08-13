package com.woowacourse.zzinbros.user;

import com.woowacourse.zzinbros.user.dto.UserRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void createUser() {
        UserRequestDto userRequestDto = new UserRequestDto(
                UserTest.BASE_NAME,
                UserTest.BASE_EMAIL,
                UserTest.BASE_PASSWORD);
        User user = userRequestDto.toEntity();
        given(userRepository.save(user)).willReturn(user);

        User savedUser = userService.add(userRequestDto);
        verify(userRepository, times(1)).save(savedUser);
    }
}