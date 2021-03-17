package com.kuke.videomeeting.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

        User user = User.createUser(uid, password, username, nickname, roles);

        // when
        em.persist(user);
        em.flush();
        em.clear();

        // then
        User result = em.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", user.getId())
                .getSingleResult();
        assertThat(result.getUid()).isEqualTo(uid);
    }


}