package com.kuke.videomeeting.advice;

import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return responseService.getFailResult(-1006, "로그인에 실패하였습니다. 5회 이상 로그인에 실패 할 시, 계정은 잠금 처리됩니다.");
    }

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result messageNotFoundException() {
        return responseService.getFailResult(-1007, "메시지를 찾을 수 없습니다.");
    }

    @ExceptionHandler(NotResourceOwnerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result notResourceOwnerException() {
        return responseService.getFailResult(-1007, "해당 자원의 소유자가 아닙니다.");
    }

    @ExceptionHandler(FriendNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result friendNotFoundException() {
        return responseService.getFailResult(-1008, "해당 친구 관계를 찾을 수 없습니다.");
    }

    @ExceptionHandler(AlreadyFriendException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result alreadyFriendException() {
        return responseService.getFailResult(-1009, "이미 친구로 등록되었습니다.");
    }

    @ExceptionHandler(CreateRoomFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result createRoomFailureException() {
        return responseService.getFailResult(-1010, "방 생성에 실패하였습니다.");
    }

    @ExceptionHandler(DestroyRoomFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result destroyRoomFailureException() {
        return responseService.getFailResult(-1011, "방 파괴에 실패하였습니다.");
    }

    @ExceptionHandler(ReadAllRoomsFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result readAllRoomsFailureException() {
        return responseService.getFailResult(-1012, "방 목록 조회에 실패하였습니다.");
    }

    @ExceptionHandler(PasswordNotMatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result passwordNotMatchException() {
        return responseService.getFailResult(-1013, "비밀번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return responseService.getFailResult(-1014, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result fileUploadFailureException() {
        return responseService.getFailResult(-1015, "파일 업로드를 실패하였습니다.");
    }

    @ExceptionHandler(SendMailFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result sendMailFailureException() {
        return responseService.getFailResult(-1016, "메일 전송에 실패하였습니다.");
    }

    @ExceptionHandler(UserCodeNotMatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userCodeNotMatchException() {
        return responseService.getFailResult(-1017, "사용자 코드가 일치하지않습니다.");
    }

    @ExceptionHandler(LockedAccountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result lockedAccountException() {
        return responseService.getFailResult(-1018, "계정이 잠겼습니다. 비밀번호 분실 기능을 이용해서 비밀번호를 변경해주세요.");
    }

}
