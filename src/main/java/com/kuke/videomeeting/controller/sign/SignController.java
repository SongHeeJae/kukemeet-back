package com.kuke.videomeeting.controller.sign;

import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.model.dto.user.UserLoginRequestDto;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.sign.SignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Api(value = "Sign Controller", tags = {"Sign"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignController {
    private final ResponseService responseService;
    private final SignService signService;

    @ApiOperation(value="회원가입", notes = "회원가입을 한다.")
    @PostMapping(value = "/sign/register")
    public Result register(@RequestBody UserRegisterRequestDto requestDto) {
        signService.register(requestDto);
        return responseService.getSuccessResult();
    }

    @ApiOperation(value="로그인", notes = "로그인을 한다.")
    @PostMapping(value = "/sign/login")
    public Result login(@RequestBody UserLoginRequestDto requestDto) {
        return responseService.getSingleResult(signService.login(requestDto));
    }
}
