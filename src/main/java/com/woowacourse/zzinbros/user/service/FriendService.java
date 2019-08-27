package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
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
    private final UserService userService;

    public FriendService(FriendRepository friendRepository, UserService userService) {
        this.friendRepository = friendRepository;
        this.userService = userService;
    }

    public boolean sendFriendRequest(final UserResponseDto requestUser, final FriendRequestDto friendRequested) {
        User from = userService.findUserById(requestUser.getId());
        User to = userService.findUserById(friendRequested.getRequestFriendId());
        if (!friendRepository.existsBySenderAndReceiver(from, to)) {
            friendRepository.save(Friend.of(from, to));
            return true;
        }
        throw new AlreadyFriendRequestExist("Already Friend Request");
    }

    public Set<UserResponseDto> findFriendsByUser(final long id) {
        User owner = userService.findUserById(id);
        return collectToUserResponseDto(owner.getFriends());
    }

    public Set<UserResponseDto> findFriendRequestsByUser(final UserResponseDto loginUserDto) {
        User owner = userService.findLoggedInUser(loginUserDto);
        return collectToUserResponseDto(owner.getRequestSenders());
    }

    public Set<UserResponseDto> findFriendRequestsByUserId(final long id) {
        User owner = userService.findUserById(id);
        return collectToUserResponseDto(owner.getRequestSenders());
    }

    private Set<UserResponseDto> collectToUserResponseDto(Set<User> users) {
        return users.stream()
                .map(user -> new UserResponseDto(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toSet());
    }
}
