package com.kuke.videomeeting.controller.exception;

import com.kuke.videomeeting.advice.exception.AuthenticationEntryPointException;
import com.kuke.videomeeting.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
public class ExceptionController {
    @GetMapping(value = "/entrypoint")
    public void entryPointException() {
        throw new AuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public void accessDeniedException() {
        throw new AccessDeniedException("");
    }
}
