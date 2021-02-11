package com.kuke.videomeeting.advice;

import com.kuke.videomeeting.advice.exception.AuthenticationEntryPointException;
import com.kuke.videomeeting.advice.exception.UserNicknameAlreadyExistsException;
import com.kuke.videomeeting.advice.exception.UserUidAlreadyExistsException;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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
        return responseService.getFailResult(-1003, "이미 해당 유저 아이디가 있습니다.");
    }

    @ExceptionHandler(UserNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userNicknameAlreadyExistsException() {
        return responseService.getFailResult(-1004, "이미 해당 닉네임이 있습니다.");
    }
}
