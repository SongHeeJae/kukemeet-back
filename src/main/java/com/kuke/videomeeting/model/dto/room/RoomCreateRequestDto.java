package com.kuke.videomeeting.model.dto.room;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomCreateRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String pin;
}
