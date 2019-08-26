package com.woowacourse.zzinbros.user.service;

import com.woowacourse.zzinbros.user.domain.Friend;
import com.woowacourse.zzinbros.user.domain.FriendMatcher;
import com.woowacourse.zzinbros.user.domain.User;
import com.woowacourse.zzinbros.user.domain.repository.FriendRepository;
import com.woowacourse.zzinbros.user.dto.FriendRequestDto;
import com.woowacourse.zzinbros.user.dto.UserResponseDto;
import com.woowacourse.zzinbros.user.exception.AlreadyFriendRequestExist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserService userService;
    private final FriendMatcher friendMatcher;

    public FriendService(FriendRepository friendRepository, UserService userService, FriendMatcher friendMatcher) {
        this.friendRepository = friendRepository;
        this.userService = userService;
        this.friendMatcher = friendMatcher;
    }

    public boolean sendFriendRequest(final UserResponseDto requestUser, final FriendRequestDto friendRequested) {
        User from = userService.findUserById(requestUser.getId());
        User to = userService.findUserById(friendRequested.getRequestFriendId());
        if (!friendRepository.existsByFromAndTo(from, to)) {
            friendRepository.save(Friend.of(from, to));
            return true;
        }
        throw new AlreadyFriendRequestExist("Already Friend Request");
    }

    public Set<UserResponseDto> findFriendByUser(final long id) {
        User owner = userService.findUserById(id);
        Set<Friend> friends = friendRepository.findByFrom(owner);
        return friendMatcher.collectFriends(friends, owner, (from, to) ->
                friendRepository.existsByFromAndTo(from, to) && friendRepository.existsByFromAndTo(to, from));
    }

    public Set<UserResponseDto> findFriendRequestsByUser(final UserResponseDto loginUserDto) {
        User owner = userService.findLoggedInUser(loginUserDto);
        Set<Friend> friends = friendRepository.findByTo(owner);
        return friendMatcher.collectFriendRequests(friends, owner, (from, to) ->
                !friendRepository.existsByFromAndTo(from, to) && friendRepository.existsByFromAndTo(to, from));
    }
}
