package com.kuke.videomeeting.controller.social;

import com.kuke.videomeeting.advice.exception.NotRegisteredProviderException;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.model.dto.social.SocialTokenRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.social.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

@Api(value = "Social Controller", tags = {"Social"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SocialController {
    private final KakaoService kakaoService;
    private final ResponseService responseService;

    @ApiOperation(value="소셜 서비스 별 토큰 획득", notes = "소셜 서비스 별 토큰을 획득한다.")
    @PostMapping(value = "/social/get-token-by-provider")
    public Result getTokenByProvider(
            @Valid @RequestBody SocialTokenRequestDto requestDto) {
        if(Objects.equals(requestDto.getProvider(), "kakao")) {
            return responseService.getSingleResult(kakaoService.getKakaoTokenInfo(requestDto.getCode()));
        }
        throw new NotRegisteredProviderException();
    }
}
