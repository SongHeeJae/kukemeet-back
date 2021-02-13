package com.kuke.videomeeting.repository.message;

import com.kuke.videomeeting.domain.ReceivedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceivedMessageRepository extends JpaRepository<ReceivedMessage, Long> {
}
