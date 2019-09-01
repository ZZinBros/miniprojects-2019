package com.woowacourse.zzinbros.user.service;

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
        List<UserResponseDto> semiUsers = Arrays.asList(
                new UserResponseDto(1L, "name1", "name1@email.com"),
                new UserResponseDto(2L, "name2", "name2@email.com")
        );

        given(userService.findAll()).willReturn(semiUsers);
        Set<UserResponseDto> expected = new HashSet<>(semiUsers);
        Set<UserResponseDto> actual = searchService.search("nam");
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("틀린 검색어로 이름 목록을 찾지 못한다")
    void test2() {
        List<UserResponseDto> semiUsers = Arrays.asList(
                new UserResponseDto(1L, "name1", "name1@email.com"),
                new UserResponseDto(2L, "name2", "name2@email.com")
        );

        given(userService.findAll()).willReturn(semiUsers);
        Set<UserResponseDto> expected = new HashSet<>();
        Set<UserResponseDto> actual = searchService.search("not");
        assertEquals(expected, actual);
    }
}