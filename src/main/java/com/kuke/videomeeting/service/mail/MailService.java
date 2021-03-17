package com.kuke.videomeeting.service.mail;

import com.kuke.videomeeting.advice.exception.SendMailFailureException;
import com.kuke.videomeeting.advice.exception.UserNotFoundException;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.user.UserSendEmailRequestDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    @Transactional
    public void sendCodeEmailForForgottenPassword(String to, String code) {
        sender.send(createTemplateForForgottenPassword(to, code));
    }

    private MimeMessage createTemplateForForgottenPassword(String uid, String code) {
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(uid);
            helper.setSubject("KUKE meet 비밀번호 분실 안내 이메일입니다.");
            helper.setText("안녕하세요. KUKE meet 입니다.<br/>" +
                    "본인 확인 란에 다음 코드를 기입해주세요.<br/>" +
                    "<strong>" + code + "</strong><br/>" +
                    "위 코드의 유효 시간은 5분입니다. 코드가 만료되었으면, 다시 발급해주세요.", true);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        throw new SendMailFailureException();
    }
}
