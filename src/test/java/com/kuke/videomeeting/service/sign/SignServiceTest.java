package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.*;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
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
        verify(jwtTokenProvider).createRefreshToken(anyString());
        assertThat(result.getInfo().getUid()).isEqualTo(requestDto.getUid());
    }

    @Test
    public void refreshTokenTest() {
        // given
        String refreshToken = "Bearer refreshToken";
        String newAccessToken = "Bearer newAccessToken";
        String newRefreshToken = "Bearer newRefreshToken";
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeRefreshToken(refreshToken);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(jwtTokenProvider.removeType(anyString())).willReturn("refreshToken");
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getUserId(anyString())).willReturn("1");
        given(jwtTokenProvider.createToken(anyString())).willReturn(newAccessToken);
        given(jwtTokenProvider.createRefreshToken(anyString())).willReturn(newRefreshToken);

        // when
        UserLoginResponseDto result = signService.refreshToken(refreshToken);

        // then
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(result.getRefreshToken()).isEqualTo(newRefreshToken);
        assertThat(result.getInfo().getUid()).isEqualTo("uid");
    }

    @Test
    public void refreshTokenExceptionByInvalidateTokenTest() {
        // given
        String refreshToken = "Bearer refreshToken";
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeRefreshToken(refreshToken);
        given(jwtTokenProvider.removeType(anyString())).willReturn("refreshToken");
        given(jwtTokenProvider.validateToken(anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    public void refreshTokenExceptionByDifferentTokenTest() {
        // given
        String refreshToken = "Bearer refreshToken";
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeRefreshToken(refreshToken + "different");
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(jwtTokenProvider.removeType(anyString())).willReturn("refreshToken");
        given(jwtTokenProvider.validateToken(anyString())).willReturn(true);
        given(jwtTokenProvider.getUserId(anyString())).willReturn("1");

        // when, then
        assertThatThrownBy(() -> signService.refreshToken(refreshToken)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    public void logout() {
        // given
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeRefreshToken("refresh-token");
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));

        // when
        signService.logout(anyLong());

        // then
        assertThat(user.getRefreshToken()).isEqualTo("");

    }

    @Test
    public void changePasswordTest() {
        // given
        String current = "current";
        String next = "next";
        User user = User.createUser("uid", current, "username", "nickname", null, null);
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(passwordEncoder.encode(anyString())).willReturn(next);

        // when
        signService.changePassword(1L, new UserChangePasswordRequestDto(current, next));

        // then
        assertThat(user.getPassword()).isEqualTo(next);
    }

    @Test
    public void changePasswordExceptionByNotMatchPasswordTest() {
        // given
        String current = "current";
        String next = "next";
        User user = User.createUser("uid", current, "username", "nickname", null, null);
        given(userRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(user));
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.changePassword(1L, new UserChangePasswordRequestDto(current, next)))
                .isInstanceOf(PasswordNotMatchException.class);
    }

    @Test
    public void changeForgottenPasswordTest() {
        // given
        String nextPassword = "nextPassword";
        String code = "code";
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeCode(code);
        given(userRepository.findByUid(anyString()))
                .willReturn(Optional.ofNullable(user));
        given(passwordEncoder.encode(anyString())).willReturn(nextPassword);

        // when
        signService.changeForgottenPassword(new UserChangeForgottenPasswordRequestDto("uid", code, nextPassword));

        // then
        assertThat(user.getPassword()).isEqualTo(nextPassword);
    }

    @Test
    public void changeForgottenPasswordExceptionByNotMatchCodeTest() {
        // given
        String nextPassword = "nextPassword";
        String code = "code";
        String notMatchCode =  "notMatchCode";
        User user = User.createUser("uid", "password", "username", "nickname", null, null);
        user.changeCode(code);
        given(userRepository.findByUid(anyString()))
                .willReturn(Optional.ofNullable(user));

        // when, then
        assertThatThrownBy(() -> {
            signService.changeForgottenPassword(new UserChangeForgottenPasswordRequestDto("uid", notMatchCode, nextPassword));
        }).isInstanceOf(UserCodeNotMatchException.class);
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