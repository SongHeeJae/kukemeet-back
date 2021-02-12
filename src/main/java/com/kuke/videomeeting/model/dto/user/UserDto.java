package com.kuke.videomeeting.model.dto.user;

import com.kuke.videomeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {
    private Long id;
    private String uid;
    private String username;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static UserDto convertUserToDto(User user) {
        return new UserDto(user.getId(), user.getUid(), user.getUsername(), user.getNickname(), user.getCreatedAt(), user.getModifiedAt());
    }
}
