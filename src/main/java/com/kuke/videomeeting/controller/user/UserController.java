package com.kuke.videomeeting.controller.user;

import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Controller", tags = {"User"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final ResponseService responseService;
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public Result readUser(@PathVariable Long userId) {
        return responseService.getSingleResult(userService.readUser(userId));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/users/{userId}")
    public Result deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return responseService.getSuccessResult();
    }

}
