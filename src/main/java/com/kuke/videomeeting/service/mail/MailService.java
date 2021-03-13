package com.kuke.videomeeting.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;

@RequiredArgsConstructor
public class MailService {
    private JavaMailSender javaMailSender;

    public void sendMail() {

    }
}
