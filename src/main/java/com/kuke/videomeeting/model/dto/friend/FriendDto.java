package com.kuke.videomeeting.model.dto.friend;

import com.kuke.videomeeting.domain.Friend;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.model.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendDto implements Serializable {
    Long id;
    UserDto user;

    public static FriendDto convertFriendToDto(Friend friend) {
        return new FriendDto(friend.getId(), UserDto.convertUserToDto(friend.getTo()));
    }
}
