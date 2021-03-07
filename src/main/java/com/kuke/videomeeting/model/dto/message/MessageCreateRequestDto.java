package com.kuke.videomeeting.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageCreateRequestDto {
    @NotBlank
    private String msg;

    @NotNull
    private Long receiverId;
}
