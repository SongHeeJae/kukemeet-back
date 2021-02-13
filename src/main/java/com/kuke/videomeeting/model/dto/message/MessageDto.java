package com.kuke.videomeeting.model.dto.message;

import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageDto {
    private Long id;
    private String msg;
    private UserInfo sender;
    private UserInfo receiver;
    private LocalDateTime createdAt;

    @AllArgsConstructor
    private static class UserInfo {
        private Long id;
        private String username;
        private String nickname;
    }

    public static MessageDto convertMessageToDto(Message message, User sender, User receiver) {
        return new MessageDto(message.getId(), message.getMsg(),
                new UserInfo(sender.getId(), sender.getUsername(), sender.getNickname()),
                new UserInfo(receiver.getId(), receiver.getUsername(), receiver.getNickname()),
                message.getCreatedAt());
    }
}
