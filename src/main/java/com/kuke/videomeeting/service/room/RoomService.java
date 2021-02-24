package com.kuke.videomeeting.service.room;

import com.google.gson.Gson;
import com.kuke.videomeeting.advice.exception.CreateRoomFailureException;
import com.kuke.videomeeting.advice.exception.DestroyRoomFailureException;
import com.kuke.videomeeting.advice.exception.ReadAllRoomsFailureException;
import com.kuke.videomeeting.model.dto.room.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RestTemplate restTemplate;
    private final int publishers = 100;
    private final Gson gson;

    @Value("${janus.admin.secret}")
    private String adminSecret;

    @Value("${janus.admin.server}")
    private String adminServer;

    @Value("${janus.room.secret}")
    private String roomSecret;

    public RoomCreateResponseDto createRoom(RoomCreateRequestDto requestDto) {
        try {
            HttpEntity<String> request = generateRequest(generateJsonForCreatingRoom(requestDto));
            ResponseEntity<String> response = sendPostRequest(request);
            RoomCreateResultDto result = gson.fromJson(response.getBody(), RoomCreateResultDto.class);
            return result.getResponse();
        } catch(Exception e) {
            throw new CreateRoomFailureException();
        }

    }

    public List<RoomDto> readAllRooms() {
        try {
            HttpEntity<String> request = generateRequest(generateJsonForRoomList());
            ResponseEntity<String> response = sendPostRequest(request);
            RoomListResultDto result = gson.fromJson(response.getBody(), RoomListResultDto.class);
            return result.getResponse().getList();
        } catch (Exception e) {
            throw new ReadAllRoomsFailureException();
        }
    }

    public void destroyRoom(String room) {
        try {
            HttpEntity<String> request = generateRequest(generateJsonForDestructionRoom(room));
            sendPostRequest(request);
        } catch(Exception e) {
            throw new DestroyRoomFailureException();
        }

    }

    private HttpEntity<String> generateRequest(String json) {
        return new HttpEntity<>(json, generateApplicationJsonHeader());
    }

    private HttpHeaders generateApplicationJsonHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ResponseEntity<String> sendPostRequest(HttpEntity<String> request) throws Exception {
        return restTemplate.postForEntity(adminServer, request, String.class);
    }

    private String generateJsonForCreatingRoom(RoomCreateRequestDto requestDto) {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{" +
                        "\"request\":\"create\"," +
                        "\"description\":\"" + requestDto.getDescription() + "\"," +
                        "\"pin\":\"" + requestDto.getPin() + "\"," +
                        "\"publishers\":" + publishers +
                    "}" +
                "}";
    }

    private String generateJsonForRoomList() {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{\"request\":\"list\"}" +
                "}";
    }

    private String generateJsonForDestructionRoom(String room) {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{\"request\":\"destroy\", \"room\":" + room + ", \"secret\":\"" + roomSecret + "\"}" +
                "}";
    }
}
