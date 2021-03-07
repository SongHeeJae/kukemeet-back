package com.kuke.videomeeting.model.dto.room;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomCreateRequestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String pin;
}
