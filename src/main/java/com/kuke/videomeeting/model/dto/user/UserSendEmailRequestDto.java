package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSendEmailRequestDto {

    @NotBlank(message = "이메일 아이디를 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String uid;

}
