package com.kuke.videomeeting.controller.sign;

import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sign")
@RequiredArgsConstructor
public class SignController {
    private final ResponseService responseService;

    @PostMapping(value = "/register")
    public Result register() {
        return responseService.getSuccessResult();
    }
}
