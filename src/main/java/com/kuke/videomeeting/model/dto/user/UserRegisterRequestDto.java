package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequestDto {
    private String uid;
    private String password;
    private String username;
    private String nickname;
    private String provider;
}
