package com.kuke.videomeeting.service.user;

import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.model.dto.user.UserSearchDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public UserDto readUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserDto.convertUserToDto(user);
    }

    public UserDto readUserByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        return UserDto.convertUserToDto(user);
    }

    public List<UserDto> readAllUsers(UserSearchDto searchDto) {
        return userRepository.findAllUsersByCondition(
                searchDto.getUid(),
                searchDto.getUsername(),
                searchDto.getNickname()
        ).stream().map(u -> UserDto.convertUserToDto(u)).collect(Collectors.toList());
    }
}
