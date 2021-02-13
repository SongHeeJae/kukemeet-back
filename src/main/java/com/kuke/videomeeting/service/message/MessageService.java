package com.kuke.videomeeting.service.message;

import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

   

}
