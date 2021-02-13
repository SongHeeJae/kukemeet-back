package com.kuke.videomeeting.advice;

import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result defaultException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return responseService.getFailResult(-1000, "오류가 발생하였습니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result accessDeniedException() {
        return responseService.getFailResult(-1001, "해당 권한이 없습니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result authenticationEntryPointException() {
        return responseService.getFailResult(-1002, "해당 권한이 없습니다.");
    }

    @ExceptionHandler(UserUidAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userUidAlreadyExistsException() {
        return responseService.getFailResult(-1003, "이미 해당 유저 아이디의 사용자가 있습니다.");
    }

    @ExceptionHandler(UserNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userNicknameAlreadyExistsException() {
        return responseService.getFailResult(-1004, "이미 해당 닉네임의 사용자가 있습니다.");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userNotFoundException() {
        return responseService.getFailResult(-1005, "해당 사용자를 찾을 수 없습니다.");
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result loginFailureException() {
        return responseService.getFailResult(-1006, "로그인에 실패하였습니다.");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result messageNotFoundException() {
        return responseService.getFailResult(-1007, "메시지를 찾을 수 없습니다.");
    }

    @ExceptionHandler(SentMessageNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result sentMessageNotFoundException() {
        return responseService.getFailResult(-1008, "메시지 발신 내역을 찾을 수 없습니다.");
    }

    @ExceptionHandler(ReceivedMessageNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result receivedMessageNotFoundException() {
        return responseService.getFailResult(-1009, "메시지 수신 내역을 찾을 수 없습니다.");
    }
}
