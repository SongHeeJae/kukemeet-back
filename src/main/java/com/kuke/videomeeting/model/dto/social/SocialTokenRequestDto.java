package com.kuke.videomeeting.model.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SocialTokenRequestDto {
    @NotBlank
    private String provider;

    @NotBlank
    private String code;
}
