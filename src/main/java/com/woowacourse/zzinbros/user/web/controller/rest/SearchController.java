package com.woowacourse.zzinbros.user.web.controller.rest;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.service.SearchService;
import com.woowacourse.zzinbros.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class SearchController {
    private final SearchService searchService;
    private final UserService userService;

    public SearchController(SearchService searchService, UserService userService) {
        this.searchService = searchService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> search(@RequestParam("name") String name, Pageable pageable) {
        List<UserResponseDto> users = userService.convertToUserResponseDto(searchService.search(name, pageable));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}