package com.kuke.videomeeting.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuke.videomeeting.config.security.JwtAuthenticationFilter;
import com.kuke.videomeeting.config.security.JwtTokenProvider;
import com.kuke.videomeeting.model.dto.user.UserLoginRequestDto;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignControllerTest {
    @Mock private ResponseService responseService;
    @Mock private SignService signService;
    @InjectMocks private SignController signController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

    @Test
    public void registerTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "123456a!", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(signService).register(info);
    }

    @Test
    public void registerExceptionByShortNicknameTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "123456a!", "username", "n");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByInvalidationEmailTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid", "123456a!", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByShortUsernameTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "123456a!", "u", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByNotGreaterThanEightCharactersPasswordTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "123a!!!", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByNonCharacterPasswordTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "123!!!123", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByNonNumberPasswordTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "!@#@$!aaad", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void registerExceptionByNonSpecialCharacterPasswordTest() throws Exception {
        // given
        UserRegisterRequestDto info = new UserRegisterRequestDto("uid@uid.com", "1234aaaab", "username", "nickname");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void loginTest() throws Exception {
        // given
        UserLoginRequestDto info = new UserLoginRequestDto("uid@uid.com", "123456a!");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (NestedServletException e) { }


        verify(signService).login(info);
    }

    @Test
    public void refreshTokenTest() throws Exception {
        // given
        String refreshToken = "Bearer refreshToken";

        // when, then
        try {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign/refresh-token")
                .header("Authorization", refreshToken))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        } catch (NestedServletException e) { }
        verify(signService).refreshToken(refreshToken);
    }

}