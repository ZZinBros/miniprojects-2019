package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.BaseTest;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static com.woowacourse.zzinbros.common.domain.TestBaseMock.mockingId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SearchServiceTest extends BaseTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    SearchService searchService;

    @Test
    @DisplayName("검색어로 이름 목록을 찾는다")
    void searchTest() {
        List<User> expected = Arrays.asList(
                mockingId(new User("name1", "name1@email.com", "123qweasd"), 1L),
                mockingId(new User("name2", "name2@email.com", "123qweasd"), 2L)
        );

        given(userRepository.findByNameNameContaining("nam", PageRequest.of(0, 2)))
                .willReturn(expected);
        List<User> actual = searchService.search("nam", PageRequest.of(0, 2));
        assertEquals(expected, actual);
    }
}