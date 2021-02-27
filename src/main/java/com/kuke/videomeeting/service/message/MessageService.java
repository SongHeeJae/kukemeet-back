package com.kuke.videomeeting.service.message;

import com.kuke.videomeeting.advice.exception.MessageNotFoundException;
import com.kuke.videomeeting.advice.exception.NotResourceOwnerException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.config.cache.CacheKey;
import com.kuke.videomeeting.domain.DeleteStatus;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.model.dto.message.SimpleMessageDto;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Cacheable(value = CacheKey.SENT_MESSAGES, key="{#userId, #lastMessageId, #limit}", unless = "#result == null")
    public Slice<SimpleMessageDto> readAllSentMessagesUsingScroll(Long userId, Long lastMessageId, int limit) {
        return messageRepository.findSentMessagesByUserIdOrderByCreatedAt(userId, lastMessageId, PageRequest.of(0, limit))
                .map(m -> SimpleMessageDto.convertSentMessageToDto(m));
    }

    @Cacheable(value = CacheKey.RECEIVED_MESSAGES, key="{#userId, #lastMessageId, #limit}", unless = "#result == null")
    public Slice<SimpleMessageDto> readAllReceivedMessagesUsingScroll(Long userId, Long lastMessageId, int limit) {
        return messageRepository.findReceivedMessagesByUserIdOrderByCreatedAt(userId, lastMessageId, PageRequest.of(0, limit))
                .map(m -> SimpleMessageDto.convertReceivedMessageToDto(m));
    }

    @Caching(evict = {
            @CacheEvict(value = CacheKey.SENT_MESSAGES, key="#userId", allEntries = true),
            @CacheEvict(value = CacheKey.RECEIVED_MESSAGES, key="#requestDto.receiverId", allEntries = true)
    })
    @Transactional
    public MessageDto createMessage(Long userId, MessageCreateRequestDto requestDto) {
        User sender = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findById(requestDto.getReceiverId()).orElseThrow(UserNotFoundException::new);
        Message message = messageRepository.save(Message.createMessage(requestDto.getMsg(), sender, receiver));
        return MessageDto.convertMessageToDto(message);
    }

    @CacheEvict(value = CacheKey.SENT_MESSAGES, key="#senderId", allEntries = true)
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

    @CacheEvict(value = CacheKey.RECEIVED_MESSAGES, key="#receiverId", allEntries = true)
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
