package com.kuke.videomeeting.service.sign;

import com.kuke.videomeeting.advice.exception.AuthenticationEntryPointException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.auth.CustomUserDetails;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseGet(
                () -> User.createUser(null, null, null, null, null, null)
        );
        return new CustomUserDetails(
                user.getId(),
                user.getUid(),
                user.getPassword(),
                user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.toString())).collect(Collectors.toList()));
    }
}
