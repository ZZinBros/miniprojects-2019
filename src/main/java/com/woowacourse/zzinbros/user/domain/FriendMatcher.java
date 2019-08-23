package com.woowacourse.zzinbros.user.domain;

import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FriendMatcher {
    public Set<UserResponseDto> parseFriends(final Set<Friend> friends, final User owner) {
        return friends.stream()
                .filter(friend -> friend.isSameWithFrom(owner))
                .map(friend -> friend.getTo())
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toSet());
    }
}
