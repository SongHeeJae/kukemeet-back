package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterByProviderRequestDto {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String provider;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=2, message="올바른 이름을 입력해주세요.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "이름은 영문 또는 한글만 입력해주세요.")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message="닉네임이 너무 짧습니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 영문 또는 한글만 입력해주세요.")
    private String nickname;
}
