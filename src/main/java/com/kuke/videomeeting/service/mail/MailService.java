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
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${domain}")
    private String domain;

    @Transactional
    public void sendCodeEmailForForgottenPassword(UserSendEmailRequestDto requestDto) {
        User user = userRepository.findByUid(requestDto.getUid()).orElseThrow(UserNotFoundException::new);
        user.changeCode(UUID.randomUUID().toString());
        sender.send(createTemplateForForgottenPassword(requestDto.getUid(), user.getCode()));
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
                    "<strong>" + code + "</strong>", true);
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        throw new SendMailFailureException();
    }
}
