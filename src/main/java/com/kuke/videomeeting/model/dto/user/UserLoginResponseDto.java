package com.kuke.videomeeting.model.dto.user;

import com.kuke.videomeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private UserDto info;

    public static UserLoginResponseDto convertUserToDto(String accessToken, String refreshToken, User user) {
        return new UserLoginResponseDto(accessToken, refreshToken, UserDto.convertUserToDto(user));
    }
}
