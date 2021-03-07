package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDto {
    String uid = "";
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "이름은 영문 또는 한글만 입력해주세요.")
    String username = "";
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "닉네임은 영문 또는 한글만 입력해주세요.")
    String nickname = "";
}
