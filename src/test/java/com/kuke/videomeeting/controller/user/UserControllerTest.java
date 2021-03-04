package com.kuke.videomeeting.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
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
class UserControllerTest {

    @Mock private ResponseService responseService;
    @Mock private UserService userService;
    @InjectMocks private UserController userController;
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void readUserTest() throws Exception {
        // given
        Long userId = 1L;

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService).readUser(userId);
    }

    @Test
    public void readUserByNicknameTest() throws Exception {
        // given
        String userNickname = "nickname";

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/nickname/{nickname}", userNickname))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(userService).readUserByNickname(userNickname);
    }

    @Test
    public void deleteUserTest() throws Exception {
        // given
        Long userId = 1L;

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{userId}", userId))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(userService).deleteUser(any(), anyLong());
    }

    @Test
    public void readAllUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(userService).readAllUsers(any());
    }
}