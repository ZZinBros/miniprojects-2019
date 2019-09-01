package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private UserService userService;

    public SearchService(UserService userService) {
        this.userService = userService;
    }

    public Set<UserResponseDto> search(String searchName) {
        List<UserResponseDto> users = userService.findAll();
        return users.stream()
                .filter(user -> user.getName().contains(searchName))
                .collect(Collectors.toSet());
    }
}
