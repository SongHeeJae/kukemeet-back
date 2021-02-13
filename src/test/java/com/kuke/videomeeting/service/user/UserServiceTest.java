package com.kuke.videomeeting.service.user;

import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void readUserTest() {

        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(User.createUser("uid", "password", "username",
                "nickname", null, null)));

        // when
        UserDto result = userService.readUser(anyLong());

        // then
        assertThat(result.getUid()).isEqualTo("uid");
    }

    @Test
    public void deleteUserTest() {

        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(User.createUser("uid", "password", "username",
                "nickname", null, null)));

        // when
        userService.deleteUser(1L);

        // then
        verify(userRepository).delete(any());
    }




}