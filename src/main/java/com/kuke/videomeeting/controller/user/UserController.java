package com.kuke.videomeeting.controller.user;

import com.kuke.videomeeting.model.auth.CustomUserDetails;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "User Controller", tags = {"User"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/users/me")
    public Result readMeByAccessToken(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        return responseService.getSingleResult(userService.readUser(userDetails.getId()));
    }

    @GetMapping("/users/nickname/{nickname}")
    public Result readUserByNickname(@PathVariable String nickname) {
        return responseService.getSingleResult(userService.readUserByNickname(nickname));
    }

    @GetMapping("/users/{userId}")
    public Result readUser(@PathVariable Long userId) {
        return responseService.getSingleResult(userService.readUser(userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/users/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return responseService.getSuccessResult();
    }

}
