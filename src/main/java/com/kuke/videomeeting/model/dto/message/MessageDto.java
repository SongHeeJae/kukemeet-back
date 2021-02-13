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
public class MessageDto {
    private Long id;
    private String msg;
    private UserDto sender;
    private UserDto receiver;
    private LocalDateTime createdAt;
    
    public static MessageDto convertMessageToDto(Message message) {
        return new MessageDto(message.getId(), message.getMsg(),
                UserDto.convertUserToDto(message.getSender()),
                UserDto.convertUserToDto(message.getReceiver()),
                message.getCreatedAt());
    }
}
