package com.kuke.videomeeting.controller.message;

import com.kuke.videomeeting.model.auth.CustomUserDetails;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.model.dto.message.MessageDto;
import com.kuke.videomeeting.model.dto.response.Result;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.message.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "Message Controller", tags = {"Message"})
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final ResponseService responseService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/messages/sent")
    public Result readAllSentMessagesUsingScroll(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "lastMessageId", required = false) Long lastMessageId,
            @RequestParam(value = "limit", defaultValue = "15") int limit) {
        Slice<MessageDto> result = messageService.readAllSentMessagesUsingScroll(userDetails.getId(), lastMessageId, limit);
        return responseService.getListResult(result.getContent(), result.hasNext());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/messages/received")
    public Result readAllReceivedMessagesUsingScroll(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(value = "lastMessageId", required = false) Long lastMessageId,
            @RequestParam(value = "limit", defaultValue = "15") int limit) {
        Slice<MessageDto> result = messageService.readAllReceivedMessagesUsingScroll(userDetails.getId(), lastMessageId, limit);
        return responseService.getListResult(result.getContent(), result.hasNext());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/messages")
    public Result createMessage(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MessageCreateRequestDto requestDto) {
        return responseService.getSingleResult(messageService.createMessage(userDetails.getId(), requestDto));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/messages/sent/{messageId}")
    public Result deleteMessageBySender(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("messageId") Long messageId) {
        messageService.deleteMessageBySender(userDetails.getId(), messageId);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access-token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/messages/received/{messageId}")
    public Result deleteMessageByReceiver(
            @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("messageId") Long messageId) {
        messageService.deleteMessageByReceiver(userDetails.getId(), messageId);
        return responseService.getSuccessResult();
    }

}
