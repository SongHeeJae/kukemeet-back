package com.kuke.videomeeting.repository.message;

import com.kuke.videomeeting.domain.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m join fetch m.sender join fetch m.receiver " +
            "where m.sender.id = :userId and m.id < :lastMessageId and " +
            "m.senderDeleteStatus = com.kuke.videomeeting.domain.DeleteStatus.N order by m.createdAt desc")
    Slice<Message> findSentMessagesByUserIdOrderByCreatedAt(@Param("userId") Long userId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);

    @Query("select m from Message m join fetch m.sender join fetch m.receiver " +
            "where m.receiver.id = :userId and m.id < :lastMessageId and " +
            "m.receiverDeleteStatus = com.kuke.videomeeting.domain.DeleteStatus.N order by m.createdAt desc")
    Slice<Message> findReceivedMessagesByUserIdOrderByCreatedAt(@Param("userId") Long userId, @Param("lastMessageId") Long lastMessageId, Pageable pageable);

}
