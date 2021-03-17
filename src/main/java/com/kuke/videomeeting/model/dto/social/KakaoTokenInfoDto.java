package com.kuke.videomeeting.model.dto.social;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KakaoTokenInfoDto {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Long expires_in;
    private Long refresh_token_expires_in;
}
