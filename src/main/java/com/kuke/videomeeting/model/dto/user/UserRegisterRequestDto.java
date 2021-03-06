package com.kuke.videomeeting.model.dto.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequestDto {
    private String uid;
    private String password;
    private String username;
    private String nickname;
}