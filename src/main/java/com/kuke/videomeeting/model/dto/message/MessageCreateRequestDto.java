package com.kuke.videomeeting.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageCreateRequestDto {
    private String msg;
    private Long receiverId;
}
