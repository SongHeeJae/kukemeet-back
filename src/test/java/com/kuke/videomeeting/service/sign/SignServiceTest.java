package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.advice.exception.UserUidAlreadyExistsException;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserLoginRequestDto;
import com.kuke.videomeeting.model.dto.user.UserLoginResponseDto;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    @InjectMocks private SignService signService;
    @Mock PasswordEncoder passwordEncoder;
    @Mock private UserRepository userRepository;
    @Mock private JwtTokenProvider jwtTokenProvider;

    @Test
    public void registerTest() {
        // given
        UserRegisterRequestDto requestDto = createUserRegisterRequestDto();
        given(userRepository.findById(anyLong())).willReturn(createUserEntityByUserRegisterRequest(requestDto));

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
        given(userRepository.findByUid(requestDto.getUid())).willReturn(createUserEntityByUserRegisterRequest(requestDto));

        // when, then
        assertThatThrownBy(() -> signService.register(requestDto)).isInstanceOf(UserUidAlreadyExistsException.class);
    }

    @Test
    public void duplicateRegisterExceptionByNickname() {
        // given
        UserRegisterRequestDto requestDto = createUserRegisterRequestDto();
        given(userRepository.findByNickname(requestDto.getNickname())).willReturn(createUserEntityByUserRegisterRequest(requestDto));

        // when, then
        assertThatThrownBy(() -> signService.register(requestDto)).isInstanceOf(UserNicknameAlreadyExistsException.class);
    }

    @Test
    public void loginTest() {

        // given
        UserLoginRequestDto requestDto = createUserLoginRequestDto();
        Optional<User> loginUser = createUserEntityByUserLoginRequest(requestDto);
        given(userRepository.findByUid(requestDto.getUid())).willReturn(loginUser);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // when
        UserLoginResponseDto result = signService.login(requestDto);

        // then
        verify(jwtTokenProvider).createToken(anyString());
        verify(jwtTokenProvider).createRefreshToken();
        assertThat(result.getInfo().getUid()).isEqualTo(requestDto.getUid());
    }

    private Optional<User> createUserEntityByUserRegisterRequest(UserRegisterRequestDto requestDto) {
        return Optional.ofNullable(User.createUser(
                requestDto.getUid(), requestDto.getPassword(), requestDto.getUsername(), requestDto.getNickname(),
                null, Collections.singletonList(Role.ROLE_NORMAL))
        );
    }

    private Optional<User> createUserEntityByUserLoginRequest(UserLoginRequestDto requestDto) {
        return Optional.ofNullable(User.createUser(
                requestDto.getUid(), requestDto.getPassword(), "username", "nickname",
                null, Collections.singletonList(Role.ROLE_NORMAL))
        );
    }

    private UserLoginRequestDto createUserLoginRequestDto() {
        return new UserLoginRequestDto("uid", "password");
    }

    private UserRegisterRequestDto createUserRegisterRequestDto() {
        return new UserRegisterRequestDto("uid", "password", "username", "nickname");
    }
}