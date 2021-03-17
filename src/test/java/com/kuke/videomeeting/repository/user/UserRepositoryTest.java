package com.kuke.videomeeting.repository.user;

import com.kuke.videomeeting.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {
    @Autowired private UserRepository userRepository;

    @Test
    public void findAllUsersByConditionTest() {
        // given
        createUserEntity("gmlwo308", "heejae", "kuke");
        createUserEntity("gmlwo30", "heejae", "kuk");
        createUserEntity("lwo30", "33333", "44444");
        String uid = "mlwo30";
        String username = "eeja";
        String nickname = "uk";

        // when
        List<User> result = userRepository.findAllUsersByCondition(uid, username, nickname);

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findAllUsersByConditionUsingUidTest() {
        // given
        createUserEntity("gmlwo308", "heejae1", "kuke1");
        createUserEntity("gmlwo803", "heejae2", "kuke2");
        createUserEntity("803gmlwo", "heejae3", "kuke3");
        createUserEntity("gmlwo309", "heejae4", "kuke4");
        String uid = "gmlwo3";

        // when
        List<User> result = userRepository.findAllUsersByCondition(uid, "", "");

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findAllUsersByConditionUsingUsernameTest() {
        // given
        createUserEntity("gmlwo308", "heejae1", "kuke1");
        createUserEntity("gmlwo803", "heejae2", "kuke2");
        createUserEntity("803gmlwo", "heejae2", "kuke3");
        createUserEntity("gmlwo309", "heejae3", "kuke4");
        String username = "jae2";

        // when
        List<User> result = userRepository.findAllUsersByCondition("", username, "");

        // then
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void findAllUsersByConditionUsingNicknameTest() {
        // given
        createUserEntity("gmlwo308", "heejae1", "kuke1");
        createUserEntity("gmlwo803", "heejae2", "kuke2");
        createUserEntity("803gmlwo", "heejae2", "kuke23");
        createUserEntity("gmlwo309", "heejae3", "kuke24");
        String nickname = "kuke2";

        // when
        List<User> result = userRepository.findAllUsersByCondition("", "", nickname);

        // then
        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    private User createUserEntity(String uid, String username, String nickname) {
        return userRepository.save(User.createUser(uid, "password", username, nickname, null));
    }
}