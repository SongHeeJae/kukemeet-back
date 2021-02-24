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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestroyEmptyRoomTask implements Runnable{

    private final RestTemplate restTemplate;
    private final Gson gson;
    private final Set<String> prevEmptyRooms; // 이전 검사에서 비어있다고 나온 방들
    private final long detectEmptyRoomPeriod = 1000L * 60; // 해당 주기마다 빈 방 검사

    @Value("${janus.admin.secret}")
    private String adminSecret;

    @Value("${janus.admin.server}")
    private String adminServer;

    @Value("${janus.room.secret}")
    private String roomSecret;


    @Override
    public void run() {
        HttpEntity<String> videoroomListRequest = getRequestForVideoroomList();

        while(true) {
            try {
                Thread.sleep(detectEmptyRoomPeriod);

                List<JanusVideoroomInfo> list = getVideoroomList(videoroomListRequest);
                List<String> emptyRoomList = list.stream().filter(i -> i.getNum_participants() == 0)
                        .map(i -> i.getRoom()).collect(Collectors.toList()); // 빈 방 목록

                List<String> destroyRoomList = emptyRoomList.stream() // 이전 검사도 비어있어서 파괴해야할 방 목록
                        .filter(i -> prevEmptyRooms.contains(i)).collect(Collectors.toList());
                destroyRoom(destroyRoomList);

                prevEmptyRooms.clear();
                prevEmptyRooms.addAll(emptyRoomList);
                prevEmptyRooms.removeAll(destroyRoomList); // 이미 파괴한 방은 제거

            } catch (Exception e) {
                // empty
            }
        }


    }

    private void destroyRoom(List<String> rooms) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        for (String room : rooms) {
            if(room.equals("1234") || room.equals("5678")) continue; // demo room
            sendPostRequest(getRequestForDestructionRoom(room, headers));
        }
    }

    private HttpEntity<String> getRequestForDestructionRoom(String room, HttpHeaders headers) {
        return new HttpEntity<>(getJsonForDestructionVideoroom(room), headers);
    }

    private List<JanusVideoroomInfo> getVideoroomList(HttpEntity<String> request) throws Exception {
        ResponseEntity<String> response = sendPostRequest(request);
        JanusVideoroomListResult result = gson.fromJson(response.getBody(), JanusVideoroomListResult.class);
        return result.getResponse().getList();
    }

    private HttpEntity<String> getRequestForVideoroomList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(getJsonForVideoroomList(), headers);
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

    private String getJsonForDestructionVideoroom(String room) {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{\"request\":\"destroy\", \"room\":" + room + "}" +
//                "\"request\":{\"request\":\"destroy\", \"room\":" + room + ", \"secret\":\"" + roomSecret + "\"}" +
                "}";
    }

    private ResponseEntity<String> sendPostRequest(HttpEntity<String> request) throws Exception {
        return restTemplate.postForEntity(adminServer, request, String.class);
    }
}
