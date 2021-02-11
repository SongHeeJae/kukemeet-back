package com.kuke.videomeeting.repository.user;

import com.kuke.videomeeting.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
