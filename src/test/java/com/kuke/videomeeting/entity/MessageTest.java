package com.kuke.videomeeting.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MessageTest {

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void beforeEach() {
        User sender = User.createUser("sender", "1234", "sender", "sender", null,  Collections.singletonList(Role.ROLE_NORMAL));
        User receiver = User.createUser("receiver", "1234", "receiver", "receiver", null,  Collections.singletonList(Role.ROLE_NORMAL));
        em.persist(sender);
        em.persist(receiver);
    }

    @Test
    public void createMessageTest() {

        // given
        Message message = Message.createMessage(
                em.createQuery("select u from User u where u.uid = :uid", User.class)
                        .setParameter("uid", "sender")
                        .getSingleResult(),
                em.createQuery("select u from User u where u.uid = :uid", User.class)
                        .setParameter("uid", "receiver")
                        .getSingleResult(),
                "this is message.");

        // when
        em.persist(message);
        em.flush();
        em.clear();

        // then
        Message result = em.createQuery("select m from Message m where m.id = :id", Message.class)
                .setParameter("id", message.getId())
                .getSingleResult();
        assertThat(result.getReadingStatus()).isEqualTo(ReadingStatus.N);
        assertThat(result.getSender().getUid()).isEqualTo("sender");
        assertThat(result.getReceiver().getUid()).isEqualTo("receiver");
    }
}