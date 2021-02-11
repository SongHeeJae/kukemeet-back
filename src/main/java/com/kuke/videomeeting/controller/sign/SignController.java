package com.kuke.videomeeting.controller.sign;

import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.sign.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class SignController {
    private final ResponseService responseService;
    private final SignService signService;

    @PostMapping(value = "/register")
    public Result register(@RequestBody UserRegisterRequestDto requestDto) {
        signService.register(requestDto);
        return responseService.getSuccessResult();
    }
}
