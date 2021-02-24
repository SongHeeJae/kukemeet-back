package com.kuke.videomeeting.janus;

import com.google.gson.Gson;
import com.kuke.videomeeting.model.dto.janus.JanusVideoroomInfo;
import com.kuke.videomeeting.model.dto.janus.JanusVideoroomListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DestroyEmptyRoomTask implements Runnable{

    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${janus.admin.secret}")
    private String adminSecret;

    @Value("${janus.admin.server}")
    private String adminServer;


    @Override
    public void run() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> videoroomListRequest = new HttpEntity<>(getJsonForVideoroomList(), headers);

        while(true) {
            try {
                Thread.sleep(10000);
                List<JanusVideoroomInfo> list = getVideoroomList(videoroomListRequest);
                for (JanusVideoroomInfo janusVideoroomInfo : list) {
                    System.out.println("janusVideoroomInfo = " + janusVideoroomInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private List<JanusVideoroomInfo> getVideoroomList(HttpEntity<String> request) {
        ResponseEntity<String> response = restTemplate.postForEntity(adminServer, request, String.class);
        JanusVideoroomListResult result = gson.fromJson(response.getBody(), JanusVideoroomListResult.class);
        return result.getResponse().getList();
    }

    private String getJsonForVideoroomList() {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{\"request\":\"list\"}" +
                "}";
    }

    private String getJsonForDestructionVideoroom() {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{\"request\":\"list\"}" +
                "}";
    }
}
