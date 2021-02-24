package com.kuke.videomeeting.model.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomCreateResponseDto {
    String videoroom;
    String room;
    String permanent;
}
