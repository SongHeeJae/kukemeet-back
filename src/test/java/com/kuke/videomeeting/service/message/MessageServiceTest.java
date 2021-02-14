package com.kuke.videomeeting.service.message;

import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.domain.DeleteStatus;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {
    @InjectMocks MessageService messageService;
    @Mock MessageRepository messageRepository;
    @Mock UserRepository userRepository;

    @Test
    public void createMessageTest() {

        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("msg", 2L);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(sender));
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(receiver));
        given(messageRepository.save(any())).willReturn(Message.createMessage("msg", sender, receiver));

        // when
        MessageDto result = messageService.createMessage(1L, requestDto);

        // then
        assertThat(result.getMsg()).isEqualTo("msg");
        assertThat(result.getSender().getUid()).isEqualTo(sender.getUid());
        assertThat(result.getReceiver().getUid()).isEqualTo(receiver.getUid());
    }

    @Test
    public void deleteNotYetReceiverDeletedMessageBySenderTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when
        messageService.deleteMessageBySender(null, 1L);

        // then
        verify(messageRepository, never()).delete(message);
    }

    @Test
    public void deleteAlreadyReceiverDeletedMessageBySenderTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        message.changeReceiverDeleteStatus(DeleteStatus.Y);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when
        messageService.deleteMessageBySender(null, 1L);

        // then
        verify(messageRepository).delete(message);
    }

    @Test
    public void deleteNotYetSenderDeletedMessageByReceiverTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when
        messageService.deleteMessageByReceiver(null, 1L);

        // then
        verify(messageRepository, never()).delete(message);
    }

    @Test
    public void deleteAlreadySenderDeletedMessageByReceiverTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        message.changeSenderDeleteStatus(DeleteStatus.Y);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when
        messageService.deleteMessageByReceiver(null, 1L);

        // then
        verify(messageRepository).delete(message);
    }

    @Test
    public void deleteSentMessageThrownByNotResourceOwnerExceptionTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when, then
        assertThatThrownBy(() -> messageService.deleteMessageBySender(1L, 1L))
                .isInstanceOf(NotResourceOwnerException.class);
    }

    @Test
    public void deleteReceivedMessageThrownByNotResourceOwnerExceptionTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        Message message = Message.createMessage("msg", sender, receiver);
        given(messageRepository.findById(any())).willReturn(Optional.ofNullable(message));

        // when, then
        assertThatThrownBy(() -> messageService.deleteMessageByReceiver(1L, 1L))
                .isInstanceOf(NotResourceOwnerException.class);
    }

    @Test
    public void readAllSentMessagesUsingScrollTest() {

        // given
        given(messageRepository.findSentMessagesByUserIdOrderByCreatedAt(anyLong(), anyLong(), any())).willReturn(new SliceImpl<>(List.of()));

        // when
        messageService.readAllSentMessagesUsingScroll(1L, 1L, 1);

        // then
        verify(messageRepository).findSentMessagesByUserIdOrderByCreatedAt(anyLong(), anyLong(), any());
    }

    @Test
    public void readAllReceivedMessagesUsingScrollTest() {
        // given
        given(messageRepository.findReceivedMessagesByUserIdOrderByCreatedAt(anyLong(), anyLong(), any())).willReturn(new SliceImpl<>(List.of()));

        // when
        messageService.readAllReceivedMessagesUsingScroll(1L, 1L, 1);

        // then
        verify(messageRepository).findReceivedMessagesByUserIdOrderByCreatedAt(anyLong(), anyLong(), any());
    }

    private User createUserEntity(String name) {
        return User.createUser(name, name, name, name, null, null);
    }
}