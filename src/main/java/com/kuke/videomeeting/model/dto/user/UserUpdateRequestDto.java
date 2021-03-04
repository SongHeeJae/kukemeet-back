package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateRequestDto {
    String username;
    String nickname;
    String currentPassword;
    String nextPassword;
}
