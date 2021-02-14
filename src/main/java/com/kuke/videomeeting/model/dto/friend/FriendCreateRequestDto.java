package com.kuke.videomeeting.model.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendCreateRequestDto {
    private Long toId;
}
