package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.advice.exception.UserUidAlreadyExistsException;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.domain.Role;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
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
                        requestDto.getProvider(),
                        Collections.singletonList(Role.ROLE_NORMAL))
        );
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

}
