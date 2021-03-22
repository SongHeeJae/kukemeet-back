package com.kuke.videomeeting.model.dto.room;

import com.kuke.videomeeting.domain.Room;
import com.kuke.videomeeting.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomSimpleDto {
    private Long id;
    private String number; // 방번호
    private String title;
    private String pin;
    private String server;
    private UserDto user;

    public static RoomSimpleDto convertRoomToDto(Room room) {
        return new RoomSimpleDto(
                room.getId(),
                room.getNumber(),
                room.getTitle(),
                room.getPin(),
                room.getServer(),
                UserDto.convertUserToDto(room.getUser())
        );
    }

    public static RoomSimpleDto convertRoomToDtoWithoutPin(Room room) {
        return new RoomSimpleDto(
                room.getId(),
                room.getNumber(),
                room.getTitle(),
                null,
                room.getServer(),
                UserDto.convertUserToDto(room.getUser())
        );
    }
}
