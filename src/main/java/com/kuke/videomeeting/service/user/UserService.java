package com.kuke.videomeeting.service.user;

import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.cache.CacheHandler;
import com.kuke.videomeeting.config.cache.CacheKey;
import com.kuke.videomeeting.domain.DeleteStatus;
import com.kuke.videomeeting.domain.Friend;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.model.dto.user.UserSearchDto;
import com.kuke.videomeeting.repository.friend.FriendRepository;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final CacheHandler cacheHandler;
    private final UserRepository userRepository;

    @Caching(evict = {
            @CacheEvict(value = CacheKey.USER, key="#userId"),
            @CacheEvict(value = CacheKey.USER_DETAILS, key="#userId"),
            @CacheEvict(value = CacheKey.USERS, allEntries = true),
            @CacheEvict(value = CacheKey.FRIENDS, key="#userId"),
            @CacheEvict(value = CacheKey.SENT_MESSAGES, key ="#userId", allEntries = true),
            @CacheEvict(value = CacheKey.RECEIVED_MESSAGES, key ="#userId", allEntries = true)
    })
    @Transactional
    public void deleteUser(Long userId, Long targetId) {
        if(!userId.equals(targetId)) throw new NotResourceOwnerException();
        User user = userRepository.findById(targetId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    @Cacheable(value = CacheKey.USER, key = "#userId", unless = "#result == null")
    public UserDto readUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserDto.convertUserToDto(user);
    }

    public UserDto readUserByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        cacheHandler.createUserCache(user.getId());
        return UserDto.convertUserToDto(user);
    }

    @Cacheable(value = CacheKey.USERS, key = "{#searchDto.uid, #searchDto.username, #searchDto.nickname}", unless = "#result == null")
    public List<UserDto> readAllUsers(UserSearchDto searchDto) {
        return userRepository.findAllUsersByCondition(
                searchDto.getUid(),
                searchDto.getUsername(),
                searchDto.getNickname()
        ).stream().map(u -> UserDto.convertUserToDto(u)).collect(Collectors.toList());
    }
}
