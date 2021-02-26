package com.kuke.videomeeting.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDto {
    String uid = "";
    String username = "";
    String nickname = "";
}
