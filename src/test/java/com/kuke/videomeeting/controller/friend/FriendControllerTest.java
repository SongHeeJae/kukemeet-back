package com.kuke.videomeeting.controller.friend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuke.videomeeting.controller.user.UserController;
import com.kuke.videomeeting.model.dto.friend.FriendCreateRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.friend.FriendService;
import com.kuke.videomeeting.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FriendControllerTest {
    @Mock private ResponseService responseService;
    @Mock private FriendService friendService;
    @InjectMocks private FriendController friendController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(friendController).build();
    }

    @Test
    public void readAllMyFriendsTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/friends/me"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(friendService).readAllMyFriends(any());
    }

    @Test
    public void createFriendTest() throws Exception {

        // given
        FriendCreateRequestDto requestDto = new FriendCreateRequestDto(1L);
        String content = objectMapper.writeValueAsString(requestDto);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/friends")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(friendService).createFriend(any(), any());
    }

    @Test
    public void deleteFriendTest() throws Exception {
        // given
        Long friendId = 1L;

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/friends/{friendId}", friendId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(friendService).deleteFriend(any(), anyLong());
    }
}