package com.kuke.videomeeting.model.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendCreateRequestDto {
    @NotNull
    private Long toId;
}
