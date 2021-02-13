package com.kuke.videomeeting.domain;

import com.kuke.videomeeting.advice.exception.MessageNotFoundException;
import com.kuke.videomeeting.advice.exception.ReceivedMessageNotFoundException;
import com.kuke.videomeeting.advice.exception.SentMessageNotFoundException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.message.ReceivedMessageRepository;
import com.kuke.videomeeting.repository.message.SentMessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MessageTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired private SentMessageRepository sentMessageRepository;
    @Autowired private ReceivedMessageRepository receivedMessageRepository;
    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository.save(User.createUser("sender", "1234", "sender", "sender", null, Collections.singletonList(Role.ROLE_NORMAL)));
        userRepository.save(User.createUser("receiver", "1234", "receiver", "receiver", null, Collections.singletonList(Role.ROLE_NORMAL)));
    }

    @Test
    public void createMessageTest() {

        // given
        User sender = userRepository.findByUid("sender").orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findByUid("receiver").orElseThrow(UserNotFoundException::new);
        String msg = "this is message.";
        Message message = Message.createMessage(msg);
        SentMessage sentMessage = SentMessage.createSentMessage(sender, message);
        ReceivedMessage receivedMessage = ReceivedMessage.createReceivedMessage(receiver, message);

        // when
        messageRepository.save(message);
        sentMessageRepository.save(sentMessage);
        receivedMessageRepository.save(receivedMessage);
        em.flush();
        em.clear();

        // then
        Message resultMessage = messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new);
        SentMessage resultSentMessage = sentMessageRepository.findById(sentMessage.getId()).orElseThrow(SentMessageNotFoundException::new);
        ReceivedMessage resultReceivedMessage = receivedMessageRepository.findById(receivedMessage.getId()).orElseThrow(ReceivedMessageNotFoundException::new);

        assertThat(resultMessage.getId()).isEqualTo(resultSentMessage.getMessage().getId());
        assertThat(resultMessage.getId()).isEqualTo(resultReceivedMessage.getMessage().getId());
    }

    @Test
    public void DeleteMessageWithSentAndReceivedMessageByCascade() {
        // given
        User sender = userRepository.findByUid("sender").orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findByUid("receiver").orElseThrow(UserNotFoundException::new);
        String msg = "this is message.";
        Message message = messageRepository.save(Message.createMessage(msg));
        SentMessage sentMessage = sentMessageRepository.save(SentMessage.createSentMessage(sender, message));
        ReceivedMessage receivedMessage = receivedMessageRepository.save(ReceivedMessage.createReceivedMessage(receiver, message));
        em.flush();
        em.clear();

        // when
        messageRepository.deleteById(message.getId());

        // then
        assertThatThrownBy(() -> sentMessageRepository.findById(sentMessage.getId()).orElseThrow(SentMessageNotFoundException::new))
                .isInstanceOf(SentMessageNotFoundException.class);
        assertThatThrownBy(() -> receivedMessageRepository.findById(receivedMessage.getId()).orElseThrow(ReceivedMessageNotFoundException::new))
                .isInstanceOf(ReceivedMessageNotFoundException.class);

    }

}