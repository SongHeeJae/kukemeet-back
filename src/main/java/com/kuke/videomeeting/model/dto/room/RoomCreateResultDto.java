package com.kuke.videomeeting.model.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomCreateResultDto {
    String janus;
    String transaction;
    RoomCreateResponseDto response;
}
