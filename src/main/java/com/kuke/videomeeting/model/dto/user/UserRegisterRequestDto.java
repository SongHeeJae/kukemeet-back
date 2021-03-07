package com.kuke.videomeeting.model.dto.user;

import lombok.*;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequestDto {

    @NotBlank(message = "이메일 아이디를 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String uid;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 최소 8자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min=2, message="올바른 이름을 입력해주세요.")
    private String username;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min=2, message="닉네임이 너무 짧습니다.")
    private String nickname;
}
