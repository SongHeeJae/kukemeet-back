package com.kuke.videomeeting.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

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
        String msg = "this is message.";
        Message message = Message.createMessage(msg);

        // when
        em.persist(message);
        em.flush();
        em.clear();

        // then
        Message result = em.createQuery("select m from Message m where m.id = :id", Message.class)
                .setParameter("id", message.getId())
                .getSingleResult();
        assertThat(result.getMsg()).isEqualTo(msg);
    }
}