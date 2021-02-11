package com.kuke.videomeeting.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void createUserTest() {

        // given
        String uid = "uid";
        String password = "1234";
        String username = "username";
        String nickname = "nickname";
        List<Role> roles = Collections.singletonList(Role.ROLE_NORMAL);

        User user = User.createUser(uid, password, username, nickname, null, roles);

        // when
        em.persist(user);
        em.flush();
        em.clear();

        // then
        User result = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", user.getId())
                .getSingleResult();
        assertThat(result.getUid()).isEqualTo(uid);
        assertThat(result.getProvider()).isNull();
    }


}