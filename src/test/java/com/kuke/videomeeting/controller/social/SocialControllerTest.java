package com.kuke.videomeeting.controller.social;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kuke.videomeeting.advice.exception.NotRegisteredProviderException;
import com.kuke.videomeeting.controller.sign.SignController;
import com.kuke.videomeeting.model.dto.social.SocialTokenRequestDto;
import com.kuke.videomeeting.model.dto.user.UserRegisterRequestDto;
import com.kuke.videomeeting.service.common.ResponseService;
import com.kuke.videomeeting.service.sign.SignService;
import com.kuke.videomeeting.service.social.KakaoService;
import org.assertj.core.api.Assertions;
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
import org.springframework.web.util.NestedServletException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SocialControllerTest {
    @Mock private ResponseService responseService;
    @Mock private KakaoService kakaoService;
    @InjectMocks private SocialController socialController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(socialController).build();
    }

    @Test
    public void getTokenByKakaoProviderTest() throws Exception {
        // given
        SocialTokenRequestDto info = new SocialTokenRequestDto("kakao", "code");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/social/get-token-by-provider")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(kakaoService).getKakaoTokenInfo(anyString());
    }

    @Test
    public void getTokenByNotRegisteredProviderExceptionTest() throws Exception {
        // given
        SocialTokenRequestDto info = new SocialTokenRequestDto("none", "code");
        String content = objectMapper.writeValueAsString(info);

        // when, then
        Assertions.assertThatThrownBy(() -> {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/social/get-token-by-provider")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        }).isInstanceOf(NestedServletException.class);
    }
}

