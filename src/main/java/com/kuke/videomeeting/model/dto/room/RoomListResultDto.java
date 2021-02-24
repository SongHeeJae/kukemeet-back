package com.kuke.videomeeting.model.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomListResultDto {
    private String janus;
    private String transaction;
    private RoomListResponseDto response;
}
