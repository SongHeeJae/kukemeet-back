package com.kuke.videomeeting.config.security;

import com.kuke.videomeeting.advice.exception.AuthenticationEntryPointException;
import com.kuke.videomeeting.model.auth.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Guard {

    public boolean checkOwnUserIdByJwt(Authentication authentication, Long userId) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId() == userId;
        } catch(Exception e) {
            return false;
        }
    }
}
