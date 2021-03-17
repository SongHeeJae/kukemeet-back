package com.kuke.videomeeting.domain;

import com.kuke.videomeeting.advice.exception.MessageNotFoundException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.repository.message.MessageRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MessageTest {

    @PersistenceContext
    private EntityManager em;
    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository userRepository;

    @BeforeEach
    public void beforeEach() {
        userRepository.save(User.createUser("sender", "1234", "sender", "sender", Collections.singletonList(Role.ROLE_NORMAL)));
        userRepository.save(User.createUser("receiver", "1234", "receiver", "receiver", Collections.singletonList(Role.ROLE_NORMAL)));
    }

    @Test
    public void createMessageTest() {

        // given
        User sender = userRepository.findByUid("sender").orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findByUid("receiver").orElseThrow(UserNotFoundException::new);
        String msg = "this is message.";
        Message message = Message.createMessage(msg, sender, receiver);

        // when
        messageRepository.save(message);
        em.flush();
        em.clear();

        // then
        Message result = messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new);
        assertThat(result.getSender().getId()).isEqualTo(sender.getId());
        assertThat(result.getReceiver().getId()).isEqualTo(receiver.getId());
        assertThat(result.getMsg()).isEqualTo(msg);
        assertThat(result.getReceiverDeleteStatus()).isEqualTo(DeleteStatus.N);
        assertThat(result.getSenderDeleteStatus()).isEqualTo(DeleteStatus.N);
    }

    @Test
    public void DeleteMessageTest() {
        // given
        User sender = userRepository.findByUid("sender").orElseThrow(UserNotFoundException::new);
        User receiver = userRepository.findByUid("receiver").orElseThrow(UserNotFoundException::new);
        Message message = messageRepository.save(Message.createMessage("msg", sender, receiver));
        em.flush();
        em.clear();

        // when
        messageRepository.deleteById(message.getId());

        // then
        assertThatThrownBy(() -> messageRepository.findById(message.getId()).orElseThrow(MessageNotFoundException::new))
                .isInstanceOf(MessageNotFoundException.class);
    }

}