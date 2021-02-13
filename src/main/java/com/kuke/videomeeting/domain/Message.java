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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "message", cascade = CascadeType.ALL)
    private ReceivedMessage receivedMessage;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "message", cascade = CascadeType.ALL)
    private SentMessage sentMessage;

    public static Message createMessage(String msg) {
        Message message = new Message();
        message.msg = msg;
        return message;
    }
}
