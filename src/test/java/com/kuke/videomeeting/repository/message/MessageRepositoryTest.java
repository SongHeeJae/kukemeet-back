package com.kuke.videomeeting.repository.message;

import com.kuke.videomeeting.domain.DeleteStatus;
import com.kuke.videomeeting.domain.Message;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MessageRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private MessageRepository messageRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void findSentMessagesByUserIdOrderByCreatedAtTest() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        List<Message> messages = initMessages(6, sender, receiver);
        messages.get(0).changeSenderDeleteStatus(DeleteStatus.Y);
        em.flush(); em.clear();

        // when
        Slice<Message> result1 = messageRepository.findSentMessagesByUserIdOrderByCreatedAt(sender.getId(), Long.MAX_VALUE, PageRequest.of(0, 3));
        List<Message> content1 = result1.getContent();
        Message lastMessage1 = content1.get(content1.size() - 1);
        Slice<Message> result2 = messageRepository.findSentMessagesByUserIdOrderByCreatedAt(sender.getId(), lastMessage1.getId(), PageRequest.of(0, 3));
        List<Message> content2 = result2.getContent();

        // then
        assertThat(content1.size()).isEqualTo(3);
        assertThat(content2.size()).isEqualTo(2);
        assertThat(result2.hasNext()).isFalse();
    }

    @Test
    public void findReceivedMessagesByUserIdOrderByCreatedAt() {
        // given
        User sender = createUserEntity("sender");
        User receiver = createUserEntity("receiver");
        List<Message> messages = initMessages(6, sender, receiver);
        messages.get(0).changeReceiverDeleteStatus(DeleteStatus.Y);
        em.flush(); em.clear();

        // when
        Slice<Message> result1 = messageRepository.findReceivedMessagesByUserIdOrderByCreatedAt(receiver.getId(), Long.MAX_VALUE, PageRequest.of(0, 3));
        List<Message> content1 = result1.getContent();
        Message lastMessage1 = content1.get(content1.size() - 1);
        Slice<Message> result2 = messageRepository.findReceivedMessagesByUserIdOrderByCreatedAt(receiver.getId(), lastMessage1.getId(), PageRequest.of(0, 3));
        List<Message> content2 = result2.getContent();

        // then
        assertThat(content1.size()).isEqualTo(3);
        assertThat(content2.size()).isEqualTo(2);
        assertThat(result2.hasNext()).isFalse();
    }

    private List<Message> initMessages(int size, User sender, User receiver) {
        List<Message> messages = new ArrayList<>();
        for(int i=0; i<size; i++) messages.add(Message.createMessage("msg" + i, sender, receiver));
        messageRepository.saveAll(messages);
        return messages;
    }

    private User createUserEntity(String name) {
        return userRepository.save(User.createUser(name, name, name, name, null));
    }
}