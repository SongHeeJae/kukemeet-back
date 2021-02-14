package com.kuke.videomeeting.service.message;

import com.kuke.videomeeting.advice.exception.MessageNotFoundException;
import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.domain.DeleteStatus;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Slice<MessageDto> readAllSentMessagesUsingScroll(Long userId, Long lastMessageId, int limit) {
        return messageRepository.findSentMessagesByUserIdOrderByCreatedAt(userId, lastMessageId != null ? lastMessageId : Long.MAX_VALUE, PageRequest.of(0, limit))
                .map(m -> MessageDto.convertMessageToDto(m));
    }

    public Slice<MessageDto> readAllReceivedMessagesUsingScroll(Long userId, Long lastMessageId, int limit) {
        return messageRepository.findReceivedMessagesByUserIdOrderByCreatedAt(userId, lastMessageId != null ? lastMessageId : Long.MAX_VALUE, PageRequest.of(0, limit))
                .map(m -> MessageDto.convertMessageToDto(m));
    }

    @Transactional
    public MessageDto createMessage(Long userId, MessageCreateRequestDto requestDto) {
        User sender = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findById(requestDto.getReceiverId()).orElseThrow(UserNotFoundException::new);
        Message message = messageRepository.save(Message.createMessage(requestDto.getMsg(), sender, receiver));
        return MessageDto.convertMessageToDto(message);
    }

    @Transactional
    public void deleteMessageBySender(Long senderId, Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        if(message.getSender().getId() != senderId) throw new NotResourceOwnerException();
        if(message.getReceiverDeleteStatus().equals(DeleteStatus.Y)) {
            messageRepository.delete(message);
        } else {
            message.changeSenderDeleteStatus(DeleteStatus.Y);
        }
    }

    @Transactional
    public void deleteMessageByReceiver(Long receiverId, Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
        if(message.getReceiver().getId() != receiverId) throw new NotResourceOwnerException();
        if(message.getSenderDeleteStatus().equals(DeleteStatus.Y)) {
            messageRepository.delete(message);
        } else {
            message.changeReceiverDeleteStatus(DeleteStatus.Y);
        }
    }

}
