package com.kuke.videomeeting.service.friend;

import com.kuke.videomeeting.advice.exception.AlreadyFriendException;
import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.domain.Friend;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.friend.FriendCreateRequestDto;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.repository.friend.FriendRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {
    @InjectMocks FriendService friendService;
    @Mock FriendRepository friendRepository;
    @Mock UserRepository userRepository;

    @Test
    public void readAllMyFriendsTest() {
        // given
        given(friendRepository.findMyFriendsByUserIdOrderByUsername(anyLong())).willReturn(new SliceImpl<>(List.of()));

        // when
        friendService.readAllMyFriends(1L);

        // then
        verify(friendRepository).findMyFriendsByUserIdOrderByUsername(anyLong());
    }

    @Test
    public void createFriendTest() {
        // given
        Long fromId = 1L, toId = 2L;
        User from = createUserEntity("from");
        User to = createUserEntity("to");
        given(userRepository.findById(fromId)).willReturn(Optional.ofNullable(from));
        given(userRepository.findById(toId)).willReturn(Optional.ofNullable(to));
        given(friendRepository.findByFromAndTo(any(), any())).willReturn(Optional.ofNullable(null));
        given(friendRepository.save(any())).willReturn(Friend.createFriend(from, to));

        // when
        UserDto result = friendService.createFriend(fromId, new FriendCreateRequestDto(toId));

        // then
        assertThat(result.getUid()).isEqualTo(to.getUid());
    }

    @Test
    public void createFriendThrownByAlreadyFriendExceptionTest() {
        // given
        Long fromId = 1L, toId = 2L;
        User from = createUserEntity("from");
        User to = createUserEntity("to");
        given(userRepository.findById(fromId)).willReturn(Optional.ofNullable(from));
        given(userRepository.findById(toId)).willReturn(Optional.ofNullable(to));
        given(friendRepository.findByFromAndTo(any(), any())).willReturn(Optional.ofNullable(Friend.createFriend(from, to)));

        // when, then
        assertThatThrownBy(()->friendService.createFriend(fromId, new FriendCreateRequestDto(toId)))
                .isInstanceOf(AlreadyFriendException.class);
    }

    @Test
    public void deleteFriendTest() {
        // given
        User from = createUserEntity("from");
        User to = createUserEntity("to");
        given(friendRepository.findById(any())).willReturn(Optional.ofNullable(Friend.createFriend(from, to)));

        // when
        friendService.deleteFriend(null, 1L);

        // then
        verify(friendRepository).delete(any());
    }

    @Test
    public void deleteFriendThrownByNotResourceOwnerExceptionTest() {
        // given
        User from = createUserEntity("from");
        User to = createUserEntity("to");
        given(friendRepository.findById(any())).willReturn(Optional.ofNullable(Friend.createFriend(from, to)));

        // when, then
        assertThatThrownBy(() -> friendService.deleteFriend(1L, 1L))
            .isInstanceOf(NotResourceOwnerException.class);
    }

    private User createUserEntity(String name) {
        return User.createUser(name, name, name, name, null, null);
    }
}