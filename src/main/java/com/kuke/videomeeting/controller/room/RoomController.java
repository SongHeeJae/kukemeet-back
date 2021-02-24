package com.kuke.videomeeting.controller.room;

import com.kuke.videomeeting.model.auth.CustomUserDetails;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.model.dto.room.RoomCreateRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.room.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "Room Controller", tags = {"Room"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/rooms/{room}")
    public Result destroyRoom(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("room") String room) {
        roomService.destroyRoom(room);
        return responseService.getSuccessResult();
    }

    @GetMapping("/rooms")
    public Result readAllRooms() {
        return responseService.getListResult(roomService.readAllRooms());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/rooms")
    public Result createRoom(@RequestBody RoomCreateRequestDto requestDto) {
        return responseService.getSingleResult(roomService.createRoom(requestDto));
    }
}
