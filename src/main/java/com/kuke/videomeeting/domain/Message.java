package com.kuke.videomeeting.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends CommonEntityDate{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Lob
    @Column(nullable = false)
    private String msg;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;
    private DeleteStatus senderDeleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;
    private DeleteStatus receiverDeleteStatus;

    public static Message createMessage(String msg, User sender, User receiver) {
        Message message = new Message();
        message.msg = msg;
        message.sender = sender;
        message.receiver = receiver;
        message.senderDeleteStatus = DeleteStatus.N;
        message.receiverDeleteStatus = DeleteStatus.N;
        return message;
    }
}
