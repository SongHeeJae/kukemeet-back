package com.kuke.videomeeting.controller.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuke.videomeeting.controller.friend.FriendController;
import com.kuke.videomeeting.model.dto.message.MessageCreateRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.friend.FriendService;
import com.kuke.videomeeting.service.message.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {
    @Mock private ResponseService responseService;
    @Mock private MessageService messageService;
    @InjectMocks private MessageController messageController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    public void readAllSentMessagesUsingScrollTest() throws Exception {

        given(messageService.readAllSentMessagesUsingScroll(any(), anyLong(), anyInt()))
                .willReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), true));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/sent?limit=10&lastMessageId=3"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(messageService).readAllSentMessagesUsingScroll(any(), anyLong(), anyInt());
    }

    @Test
    public void readAllReceivedMessagesUsingScrollTest() throws Exception {

        given(messageService.readAllReceivedMessagesUsingScroll(any(), anyLong(), anyInt()))
                .willReturn(new SliceImpl<>(List.of(), PageRequest.of(0, 10), true));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/messages/received?limit=10&lastMessageId=3"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(messageService).readAllReceivedMessagesUsingScroll(any(), anyLong(), anyInt());
    }

    @Test
    public void createMessageTest() throws Exception {

        // given
        MessageCreateRequestDto requestDto = new MessageCreateRequestDto("msg", 1L);
        String content = objectMapper.writeValueAsString(requestDto);
        System.out.println("content = " + content);
        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(messageService).createMessage(any(), any());
    }

    @Test
    public void deleteMessageBySenderTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/sent/{messageId}", 1L))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(messageService).deleteMessageBySender(any(), anyLong());
    }

    @Test
    public void deleteMessageByReceiverTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/messages/received/{messageId}", 1L))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(messageService).deleteMessageByReceiver(any(), anyLong());
    }

}