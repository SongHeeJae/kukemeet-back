package com.kuke.videomeeting.service.social;

import com.google.gson.Gson;
import com.kuke.videomeeting.advice.exception.KakaoCommunicationFailureException;
import com.kuke.videomeeting.advice.exception.NotRegisteredProviderUserInfoException;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.social.KakaoIdInfoDto;
import com.kuke.videomeeting.model.dto.social.KakaoTokenInfoDto;
import com.kuke.videomeeting.model.dto.user.UserDto;
import com.kuke.videomeeting.model.dto.user.UserLoginResponseDto;
import com.kuke.videomeeting.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@RequiredArgsConstructor
@Service
public class KakaoService {
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${domain}")
    private String domain;

    @Value("${kakao.clientId}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    public String generateKakaoUid(String accessToken) {
        return "{kakao}" + getKakaoIdInfo(accessToken).getId();
    }

    public KakaoTokenInfoDto getKakaoTokenInfo(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", domain + redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("https://kauth.kakao.com/oauth/token", request, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), KakaoTokenInfoDto.class);
        }
        throw new KakaoCommunicationFailureException();
    }

    private KakaoIdInfoDto getKakaoIdInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        ResponseEntity<String> response = restTemplate.postForEntity("https://kapi.kakao.com/v2/user/me", new HttpEntity<>(null, headers), String.class);
        if(response.getStatusCode() == HttpStatus.OK) {
            return gson.fromJson(response.getBody(), KakaoIdInfoDto.class);
        }
        throw new KakaoCommunicationFailureException();
    }
}
