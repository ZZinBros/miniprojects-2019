package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SearchServiceTest {

    @MockBean
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Autowired
    SearchService searchService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @DisplayName("검색어로 이름 목록을 찾는다")
    void test() {
        List<User> users = Arrays.asList(
                mockingId(new User("name1", "name1@email.com", "123qweasd"), 1L),
                mockingId(new User("name2", "name2@email.com", "123qweasd"), 2L)
        );

        List<UserResponseDto> semiUsers = Arrays.asList(
                new UserResponseDto(1L,
                        "name1",
                        "name1@email.com",
                        "/images/default/eastjun_profile.jpg"),
                new UserResponseDto(2L,
                        "name2",
                        "name2@email.com",
                        "/images/default/eastjun_profile.jpg")
        );

        given(userRepository.findByNameNameContaining("nam", PageRequest.of(0, 2))).willReturn(users);
        Set<UserResponseDto> expected = new HashSet<>(semiUsers);
        Set<UserResponseDto> actual = searchService.search("nam", PageRequest.of(0, 2));
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("틀린 검색어로 이름 목록을 찾지 못한다")
    void test2() {
        List<User> users = Arrays.asList(
                new User("name1", "name1@email.com", "123qweasd"),
                new User("name2", "name2@email.com", "123qweasd")
        );

        given(userRepository.findByNameNameContaining("nam", PageRequest.of(0, 2)))
                .willReturn(emptyList());
        Set<UserResponseDto> expected = new HashSet<>();
        Set<UserResponseDto> actual = searchService.search("not", PageRequest.of(0, 2));
        assertEquals(expected, actual);
    }
}