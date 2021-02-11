package com.kuke.videomeeting.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String uid;
    private String password;
    private String username;
    private String nickname;
    private String provider;
}
