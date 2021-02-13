package com.kuke.videomeeting.repository.message;

import com.kuke.videomeeting.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
