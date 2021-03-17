package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.*;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EntityManager entityManager;

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
    public UserLoginResponseDto login(UserLoginRequestDto requestDto){
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(LoginFailureException::new);
        if(user.getFailureCount() >= 5) throw new LockedAccountException();
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            user.increaseFailureCount();
            entityManager.createNativeQuery("commit").executeUpdate();
            throw new LoginFailureException();
        }
        user.changeRefreshToken(createRefreshToken(user.getId()));
        user.resetFailureCount();
        return new UserLoginResponseDto(createToken(user.getId()), user.getRefreshToken(), UserDto.convertUserToDto(user));
    }
    
    @Transactional
    public UserLoginResponseDto refreshToken(String refreshToken) {
        String token = jwtTokenProvider.removeType(refreshToken);
        if(!jwtTokenProvider.validateToken(token)) throw new AccessDeniedException("");
        String userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(UserNotFoundException::new);
        if(!Objects.equals(user.getRefreshToken(), refreshToken)) throw new AccessDeniedException("");
        user.changeRefreshToken(createRefreshToken(user.getId()));
        return new UserLoginResponseDto(createToken(user.getId()), user.getRefreshToken(), UserDto.convertUserToDto(user));
    }

    @Transactional
    public void logout(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
                .changeRefreshToken("");
    }

    @Transactional
    public void changePassword(Long userId, UserChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword()))
            throw new PasswordNotMatchException();
        user.changePassword(passwordEncoder.encode(requestDto.getNextPassword()));
    }

    @Transactional
    public void changeForgottenPassword(UserChangeForgottenPasswordRequestDto requestDto) {
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(UserNotFoundException::new);
        if (!Objects.equals(user.getCode(), requestDto.getCode())) throw new UserCodeNotMatchException();
        user.changeCode("");
        user.resetFailureCount();
        user.changePassword(passwordEncoder.encode(requestDto.getNextPassword()));
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

    private String createToken(Long userId) {
        return jwtTokenProvider.createToken(String.valueOf(userId));
    }

    private String createRefreshToken(Long userId) {
        return jwtTokenProvider.createRefreshToken(String.valueOf(userId));
    }
}
