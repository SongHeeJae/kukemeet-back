package com.kuke.videomeeting.repository.user;

import com.kuke.videomeeting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);
    Optional<User> findByNickname(String nickname);
    Optional<User> findByUidAndProvider(String uid, String provider);
}
