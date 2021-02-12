package com.kuke.videomeeting.model.dto.user;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserRegisterRequestDto {
    private String uid;
    private String password;
    private String username;
    private String nickname;
    private String provider;
}
