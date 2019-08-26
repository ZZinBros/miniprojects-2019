package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Component
public class FriendMatcher {

    public Set<UserResponseDto> collectFriends(Set<Friend> friends, User owner, BiPredicate<User, User> temp) {
        return friends.stream()
                .filter(friend -> temp.test(owner, friend.getTo()))
                .map(Friend::getTo)
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toSet());
    }
}
