package com.kuke.videomeeting.service.user;

import com.kuke.videomeeting.advice.exception.PasswordNotMatchException;
import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.cache.CacheHandler;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.model.dto.user.UserSearchDto;
import com.kuke.videomeeting.model.dto.user.UserUpdateRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CacheHandler cacheHandler;
    @InjectMocks private UserService userService;

    @Test
    public void readUserTest() {

        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(User.createUser("uid", "password", "username",
                "nickname", null)));

        // when
        UserDto result = userService.readUser(anyLong());

        // then
        assertThat(result.getUid()).isEqualTo("uid");
    }

    @Test
    public void deleteUserTest() {

        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(User.createUser("uid", "password", "username",
                "nickname", null)));

        // when
        userService.deleteUser(1L);

        // then
        verify(userRepository).delete(any());
    }

    @Test
    public void readUserByNicknameTest() {
        // given
        given(userRepository.findByNickname(anyString())).willReturn(Optional.ofNullable(User.createUser("uid", "password", "username",
                "nickname", null)));

        // when
        UserDto result = userService.readUserByNickname(anyString());

        // then
        assertThat(result.getNickname()).isEqualTo("nickname");
    }
    
    @Test
    public void readAllUsersTest() {

        // given
        UserSearchDto searchDto = new UserSearchDto();
        given(userRepository.findAllUsersByCondition(anyString(), anyString(), anyString())).willReturn(Collections.emptyList());
        
        // when
        List<UserDto> result = userService.readAllUsers(searchDto);

        // then
        assertThat(result.size()).isEqualTo(0);
        verify(userRepository).findAllUsersByCondition(anyString(), anyString(), anyString());
    }

    @Test
    public void updateUserTest() {

        // given
        String current = "current";
        String next = "next";
        User user = User.createUser("uid", "password", current, current, null);
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));

        // when
        userService.updateUser(1L, new UserUpdateRequestDto(next, next));

        // then
        assertThat(user.getNickname()).isEqualTo(next);
        assertThat(user.getUsername()).isEqualTo(next);
    }

    @Test
    public void updateUserExceptionByDuplicateNicknameTest() {

        // given
        String currentNickname = "current";
        String nextNickname = "next";
        User user1 = User.createUser("uid", "password", "username", currentNickname, null);
        User user2 = User.createUser("uid", "password", "username", nextNickname, null);

        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user1));
        given(userRepository.findByNickname(anyString()))
                .willReturn(Optional.ofNullable(user2));

        // when, then
        assertThatThrownBy(() -> userService.updateUser(1L, new UserUpdateRequestDto(null, nextNickname)))
                .isInstanceOf(UserNicknameAlreadyExistsException.class);
    }







}