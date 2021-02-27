package com.kuke.videomeeting.service.friend;

import com.kuke.videomeeting.advice.exception.AlreadyFriendException;
import com.kuke.videomeeting.advice.exception.FriendNotFoundException;
import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.config.cache.CacheKey;
import com.kuke.videomeeting.domain.Friend;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.friend.FriendCreateRequestDto;
import com.kuke.videomeeting.model.dto.friend.FriendDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.repository.friend.FriendRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Cacheable(value = CacheKey.FRIENDS, key="#userId", unless = "#result == null")
    public List<FriendDto> readAllMyFriends(Long userId) {
        return friendRepository.findMyFriendsByUserIdOrderByUsername(userId).getContent()
                .stream().map(f -> FriendDto.convertFriendToDto(f)).collect(Collectors.toList());
    }

    @Caching(evict = {
            @CacheEvict(value = CacheKey.FRIENDS, key="#fromId"),
            @CacheEvict(value = CacheKey.FRIENDS, key="#requestDto.toId")
    })
    @Transactional
    public UserDto createFriend(Long fromId, FriendCreateRequestDto requestDto) {
        User from = userRepository.findById(fromId).orElseThrow(UserNotFoundException::new);
        User to = userRepository.findById(requestDto.getToId()).orElseThrow(UserNotFoundException::new);
        friendRepository.findByFromAndTo(from, to).ifPresent(f -> {
            throw new AlreadyFriendException();
        });
        return UserDto.convertUserToDto(
                friendRepository.save(Friend.createFriend(from, to)).getTo()
        );
    }

    @Caching(evict = {
            @CacheEvict(value = CacheKey.FRIENDS, key="#userId"),
            @CacheEvict(value = CacheKey.FRIENDS, key="#friendId")
    })
    @Transactional
    public void deleteFriend(Long userId, Long friendId) {
        Friend friend = friendRepository.findById(friendId).orElseThrow(FriendNotFoundException::new);
        if(friend.getFrom().getId() != userId) throw new NotResourceOwnerException();
        friendRepository.delete(friend);
    }
}
