package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Component
public class FriendMatcher {

    public Set<UserResponseDto> collectFriends(Set<Friend> friends, User owner, BiPredicate<User, User> predicate) {
        return friends.stream()
                .filter(friend -> friend.isSameWithFrom(owner))
                .filter(friend -> predicate.test(owner, friend.getTo()))
                .map(Friend::getTo)
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toSet());
    }

    public Set<UserResponseDto> collectFriendRequests(
            Set<Friend> friends,
            User owner,
            BiPredicate<User, User> predicate) {
        return friends.stream()
                .filter(friend -> friend.isSameWithTo(owner))
                .filter(friend -> predicate.test(owner, friend.getFrom()))
                .map(Friend::getFrom)
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toSet());
    }
}
