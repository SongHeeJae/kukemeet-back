package com.kuke.videomeeting.controller.friend;

import com.kuke.videomeeting.model.auth.CustomUserDetails;
import com.kuke.videomeeting.model.dto.friend.FriendCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.friend.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "Friend Controller", tags = {"Friend"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FriendController {
    private final ResponseService responseService;
    private final FriendService friendService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/friends")
    public Result readAllMyFriends(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        return responseService.getSingleResult(friendService.readAllMyFriends(userDetails.getId()));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/friends")
    public Result createFriend(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FriendCreateRequestDto requestDto) {
        System.out.println("requestDto = " + requestDto);
        return responseService.getSingleResult(friendService.createFriend(userDetails.getId(), requestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/friends/{friendId}")
    public Result deleteFriend(
            @PathVariable Long friendId,
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        friendService.deleteFriend(userDetails.getId(), friendId);
        return responseService.getSuccessResult();
    }
}
