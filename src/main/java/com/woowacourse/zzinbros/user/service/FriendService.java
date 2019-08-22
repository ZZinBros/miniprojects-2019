package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.AlreadyFriendRequestExist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;

    public FriendService(FriendRepository friendRepository) {
        this.friendRepository = friendRepository;
    }

    public Friend sendFriendRequest(final User from, final User to) {
        if (!friendRepository.existsByFromAndTo(from, to)) {
            return friendRepository.save(Friend.of(from, to));
        }
        throw new AlreadyFriendRequestExist("Already Friend Request");
    }

    public boolean checkFriend(final User user, final User other) {
        return friendRepository.existsByFromAndTo(user, other) && friendRepository.existsByFromAndTo(other, user);
    }

    public Set<UserResponseDto> findFriendByUser(final User user) {
        Set<Friend> friends = friendRepository.findByFrom(user);
        return friends.stream()
                .filter(friend -> friend.isSameWithFrom(user))
                .map(friend -> friend.getTo())
                .map(u -> new UserResponseDto(u.getId(), u.getName(), u.getEmail()))
                .collect(Collectors.toSet());
    }
}
