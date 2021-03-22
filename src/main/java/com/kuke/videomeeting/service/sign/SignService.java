package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.*;
import com.kuke.videomeeting.repository.user.UserRepository;
import com.kuke.videomeeting.service.cache.CacheService;
import com.kuke.videomeeting.service.mail.MailService;
import com.kuke.videomeeting.service.social.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MailService mailService;
    private final CacheService cacheService;
    private final KakaoService kakaoService;

    public void register(UserRegisterRequestDto requestDto) {
        validateDuplicateUserByNickname(requestDto.getNickname());
        validateDuplicateUserByUid(requestDto.getUid());
        userRepository.save(
                User.createUser(
                        requestDto.getUid(),
                        passwordEncoder.encode(requestDto.getPassword()),
                        requestDto.getUsername(),
                        requestDto.getNickname(),
                        Collections.singletonList(Role.ROLE_NORMAL))
        );
    }

    @Transactional(noRollbackFor = LoginFailureException.class)
    public UserLoginResponseDto login(UserLoginRequestDto requestDto){
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(LoginFailureException::new);
        if(user.getFailureCount() >= 5) throw new LockedAccountException();
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            user.increaseFailureCount();
            throw new LoginFailureException();
        }
        user.changeRefreshToken(createRefreshToken(user.getId()));
        user.resetFailureCount();
        return UserLoginResponseDto.convertUserToDto(createToken(user.getId()), user.getRefreshToken(), user);
    }

    public UserLoginResponseDto loginByKakao(UserLoginByProviderRequestDto requestDto) {
        String uid = kakaoService.generateKakaoUid(requestDto.getAccessToken());
        User user = userRepository.findByUid(uid).orElseThrow(NotRegisteredProviderUserInfoException::new);
        user.changeRefreshToken(createRefreshToken(user.getId()));
        return UserLoginResponseDto.convertUserToDto(createToken(user.getId()), user.getRefreshToken(), user);
    }

    public UserLoginResponseDto registerByKakao(UserRegisterByProviderRequestDto requestDto) {
        String uid = kakaoService.generateKakaoUid(requestDto.getAccessToken());
        validateDuplicateUserByUid(uid);
        validateDuplicateUserByNickname(requestDto.getNickname());
        User user = userRepository.save(
                User.createProviderUser(
                        uid,
                        requestDto.getUsername(),
                        requestDto.getNickname(),
                        Collections.singletonList(Role.ROLE_NORMAL))
        );
        user.changeRefreshToken(createRefreshToken(user.getId()));
        return UserLoginResponseDto.convertUserToDto(createToken(user.getId()), user.getRefreshToken(), user);
    }

    public UserLoginResponseDto refreshToken(String refreshToken) {
        String token = jwtTokenProvider.removeType(refreshToken);
        if(!jwtTokenProvider.validateToken(token)) throw new AccessDeniedException("");
        String userId = jwtTokenProvider.getUserId(token);
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(UserNotFoundException::new);
        if(!Objects.equals(user.getRefreshToken(), refreshToken)) throw new AccessDeniedException("");
        user.changeRefreshToken(createRefreshToken(user.getId()));
        return new UserLoginResponseDto(createToken(user.getId()), user.getRefreshToken(), UserDto.convertUserToDto(user));
    }

    public void logout(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new)
                .changeRefreshToken("");
    }

    public void changePassword(Long userId, UserChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if(!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword()))
            throw new PasswordNotMatchException();
        user.changePassword(passwordEncoder.encode(requestDto.getNextPassword()));
    }

    public void changeForgottenPassword(UserChangeForgottenPasswordRequestDto requestDto) {
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(UserNotFoundException::new);
        if (!Objects.equals(cacheService.readCodeCache(requestDto.getUid()), requestDto.getCode())) throw new UserCodeNotMatchException();
        user.resetFailureCount();
        user.changePassword(passwordEncoder.encode(requestDto.getNextPassword()));
    }

    @Transactional(readOnly = true)
    public void handleCodeEmailForForgottenPasswordUser(UserSendEmailRequestDto requestDto) {
        userRepository.findByUid(requestDto.getUid()).orElseThrow(UserNotFoundException::new);
        String code = generateCode();
        cacheService.deleteCodeCache(requestDto.getUid());
        cacheService.createCodeCache(requestDto.getUid(), code);
        mailService.sendCodeEmailForForgottenPassword(requestDto.getUid(), code);
    }

    private String generateCode() {
        return UUID.randomUUID().toString();
    }

    private void validateDuplicateUserByUid(String uid) {
        if(userRepository.findByUid(uid).isPresent()) throw new UserUidAlreadyExistsException();
    }

    private void validateDuplicateUserByNickname(String nickname) {
        if(userRepository.findByNickname(nickname).isPresent()) throw new UserNicknameAlreadyExistsException();
    }

    private String createToken(Long userId) {
        return jwtTokenProvider.createToken(String.valueOf(userId));
    }

    private String createRefreshToken(Long userId) {
        return jwtTokenProvider.createRefreshToken(String.valueOf(userId));
    }
}
