package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SearchServiceTest {

    @MockBean
    UserService userService;

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

        given(userService.findAll()).willReturn(users);
        Set<UserResponseDto> expected = new HashSet<>(semiUsers);
        Set<UserResponseDto> actual = searchService.search("nam");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("틀린 검색어로 이름 목록을 찾지 못한다")
    void test2() {
        List<User> users = Arrays.asList(
                new User("name1", "name1@email.com", "123qweasd"),
                new User("name2", "name2@email.com", "123qweasd")
        );

        given(userService.findAll()).willReturn(users);
        Set<UserResponseDto> expected = new HashSet<>();
        Set<UserResponseDto> actual = searchService.search("not");
        assertEquals(expected, actual);
    }
}