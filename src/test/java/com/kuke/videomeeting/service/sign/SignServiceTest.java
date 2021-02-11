package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.advice.exception.UserUidAlreadyExistsException;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    @InjectMocks
    private SignService signService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Test
    public void registerTest() {
        // given
        UserRegisterRequestDto requestDto = createUserRegisterRequestDto();
        given(userRepository.findById(anyLong())).willReturn(createUserEntity(requestDto));

        // when
        signService.register(requestDto);

        // then
        User result = userRepository.findById(anyLong()).orElseThrow(UserNotFoundException::new);
        assertThat(result.getNickname()).isEqualTo(requestDto.getNickname());
        assertThat(result.getRoles().get(0)).isEqualTo(Role.ROLE_NORMAL);
    }

    @Test
    public void duplicateRegisterExceptionByUid() {
        // given
        UserRegisterRequestDto requestDto = createUserRegisterRequestDto();
        given(userRepository.findByUid(requestDto.getUid())).willReturn(createUserEntity(requestDto));

        // when, then
        assertThatThrownBy(() -> signService.register(requestDto)).isInstanceOf(UserUidAlreadyExistsException.class);
    }

    @Test
    public void duplicateRegisterExceptionByNickname() {
        // given
        UserRegisterRequestDto requestDto = createUserRegisterRequestDto();
        given(userRepository.findByNickname(requestDto.getNickname())).willReturn(createUserEntity(requestDto));

        // when, then
        assertThatThrownBy(() -> signService.register(requestDto)).isInstanceOf(UserNicknameAlreadyExistsException.class);
    }

    private Optional<User> createUserEntity(UserRegisterRequestDto requestDto) {
        return Optional.ofNullable(User.createUser(
                requestDto.getUid(), requestDto.getPassword(), requestDto.getUsername(), requestDto.getNickname(),
                requestDto.getProvider(), Collections.singletonList(Role.ROLE_NORMAL))
        );
    }

    private UserRegisterRequestDto createUserRegisterRequestDto() {
        return new UserRegisterRequestDto("uid", "password", "username", "nickname", "provider");
    }
}