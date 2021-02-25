package com.kuke.videomeeting.model.dto.message;

import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SimpleMessageDto {
    private Long id;
    private String msg;
    private UserDto user;
    private LocalDateTime createdAt;

    public static SimpleMessageDto convertSentMessageToDto(Message message) {
        return new SimpleMessageDto(message.getId(), message.getMsg(),
                UserDto.convertUserToDto(message.getReceiver()),
                message.getCreatedAt());
    }

    public static SimpleMessageDto convertReceivedMessageToDto(Message message) {
        return new SimpleMessageDto(message.getId(), message.getMsg(),
                UserDto.convertUserToDto(message.getSender()),
                message.getCreatedAt());
    }
}
