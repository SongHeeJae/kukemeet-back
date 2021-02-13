package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.LoginFailureException;
import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.advice.exception.UserUidAlreadyExistsException;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.model.dto.user.UserLoginRequestDto;
import com.kuke.videomeeting.model.dto.user.UserLoginResponseDto;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void register(UserRegisterRequestDto requestDto) {
        validateDuplicateUserByNickname(requestDto.getNickname());
        validateDuplicateUserByUid(requestDto.getUid());
        userRepository.save(
                User.createUser(
                        requestDto.getUid(),
                        passwordEncoder.encode(requestDto.getPassword()),
                        requestDto.getUsername(),
                        requestDto.getNickname(),
                        null,
                        Collections.singletonList(Role.ROLE_NORMAL))
        );
    }

    @Transactional
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(LoginFailureException::new);
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) throw new LoginFailureException();
        String userId = String.valueOf(user.getId());
        String refreshToken = createRefreshToken(userId);
        user.changeRefreshToken(refreshToken);
        return new UserLoginResponseDto(jwtTokenProvider.createToken(userId), refreshToken, UserDto.convertUserToDto(user));
    }

    @Transactional
    public UserLoginResponseDto refreshToken(String refreshToken) {
        String token = jwtTokenProvider.removeType(refreshToken);
        if(!jwtTokenProvider.validateToken(token)) throw new AccessDeniedException("");
        String userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(UserNotFoundException::new);
        if(!user.getRefreshToken().equals(refreshToken)) throw new AccessDeniedException("");
        String newRefreshToken = createRefreshToken(userId);
        user.changeRefreshToken(newRefreshToken);
        return new UserLoginResponseDto(jwtTokenProvider.createToken(userId), newRefreshToken, UserDto.convertUserToDto(user));
    }



    private void validateDuplicateUserByUid(String uid) {
        if(userRepository.findByUid(uid).isPresent()) throw new UserUidAlreadyExistsException();
    }

    private void validateDuplicateUserByNickname(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent()) throw new UserNicknameAlreadyExistsException();
    }

    private void validateDuplicateUserByProvider(String uid, String provider) {
        if(StringUtils.hasText(provider) && userRepository.findByUidAndProvider(uid, provider).isPresent())
            new UserUidAlreadyExistsException();
    }

    private String createRefreshToken(String userId) {
        return jwtTokenProvider.createRefreshToken(userId);
    }

}
