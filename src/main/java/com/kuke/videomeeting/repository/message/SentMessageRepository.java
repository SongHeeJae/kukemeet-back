package com.kuke.videomeeting.repository.message;

import com.kuke.videomeeting.domain.SentMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentMessageRepository extends JpaRepository<SentMessage, Long> {
}
