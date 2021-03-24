package com.kuke.videomeeting.service.room;

import com.google.gson.Gson;
import com.kuke.videomeeting.advice.exception.*;
import com.kuke.videomeeting.domain.Room;
import com.kuke.videomeeting.domain.User;
import com.kuke.videomeeting.model.dto.room.*;
import com.kuke.videomeeting.repository.room.RoomRepository;
import com.kuke.videomeeting.repository.user.UserRepository;
import com.kuke.videomeeting.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final RoomRepository roomRepository;
    private final int publishers = 100;
    private final Gson gson;

    @Value("${janus.admin.secret}")
    private String adminSecret;

    @Value("${janus.server}")
    private List<String> server; // (+/janus: media), (+/admin: admin)

    @Value("${janus.room.secret}")
    private String roomSecret;

    @Transactional
    public RoomSimpleDto createRoom(Long userId, RoomCreateRequestDto requestDto) {
        // 방 생성이 정상적으로 처리되었을 때 DB에 저장
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String pin = generateRoomPin(), server = getServer();
        try {
            HttpEntity<String> request = generateRequest(generateJsonForCreatingRoom(requestDto, pin));
            ResponseEntity<String> response = sendPostRequest(request, server);
            if(response.getStatusCode() == HttpStatus.OK) {
                RoomCreateResultDto result = gson.fromJson(response.getBody(), RoomCreateResultDto.class);
                Room room = roomRepository.save(
                        Room.createRoom(result.getResponse().getRoom(), requestDto.getTitle(), pin, server, user)
                );
                return RoomSimpleDto.convertRoomToDto(room);
            }
        } catch (Exception e) {
            throw new CreateRoomFailureException();
        }
        throw new MediaServerCommunicationFailureException();
    }

    @Transactional(readOnly = true)
    public RoomSimpleDto readRoomInDB(String number) {
        Room room = roomRepository.findByNumber(number).orElseThrow(RoomNotFoundException::new);
        return RoomSimpleDto.convertRoomToDtoWithoutPin(room);
    }

    public Map<String, List<RoomDto>> readAllRooms() {
        try {
            Map<String, List<RoomDto>> result = new HashMap<>();
            for (String s : server) {
                HttpEntity<String> request = generateRequest(generateJsonForRoomList());
                ResponseEntity<String> response = sendPostRequest(request, s);
                RoomListResultDto listResult = gson.fromJson(response.getBody(), RoomListResultDto.class);
                result.put(s, listResult.getResponse().getList());
            }
            return result;
        } catch (Exception e) {
            throw new ReadAllRoomsFailureException();
        }
    }

    public Map<String, List<String>> readAllSessions() {
        try {
            Map<String, List<String>> result = new HashMap<>();
            for (String s : server) {
                HttpEntity<String> request = generateRequest(generateJsonForReadingAllSessions());
                ResponseEntity<String> response = sendPostRequest(request, getServer());
                SessionResultDto sessionResult = gson.fromJson(response.getBody(), SessionResultDto.class);
                result.put(s, sessionResult.getSessions());
            }
            return result;
        } catch (Exception e) {
            throw new ReadAllSessionsFailureException();
        }
    }

    @Transactional
    public void destroyRoom(String number, String server) {
        // 생성 시점에 미디어 서버와 DB는 둘다 생성이 보장되어 있음
        // 이미 제거 요청된 방이고, 미디어 서버에서 어차피 또 가져와서 삭제 요청할 것이므로
        // 예외 발생하더라도 롤백 안해도됨. DB에 있든 없든, 0명으로 나왔으니 삭제시켜야함
        try {
            roomRepository.findByNumber(number).ifPresent(r -> roomRepository.delete(r));
            fileService.deleteFilesInDirectory(number);
            HttpEntity<String> request = generateRequest(generateJsonForDestructionRoom(number));
            sendPostRequest(request, server);
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

    private ResponseEntity<String> sendPostRequest(HttpEntity<String> request, String server) throws Exception {
        return restTemplate.postForEntity(server + "/admin", request, String.class);
    }

    private String generateJsonForCreatingRoom(RoomCreateRequestDto requestDto, String pin) {
        return "{" +
                "\"janus\":\"message_plugin\", " +
                "\"plugin\":\"janus.plugin.videoroom\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\", " +
                "\"request\":{" +
                        "\"request\":\"create\"," +
                        "\"description\":\"" + requestDto.getTitle() + "\"," +
                        "\"pin\":\"" + pin + "\"," +
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

    private String generateJsonForReadingAllSessions() {
        return "{" +
                "\"janus\":\"list_sessions\", " +
                "\"transaction\":\"" + (int) (Math.random() * Integer.MAX_VALUE) + "\", " +
                "\"admin_secret\":\"" + adminSecret + "\"" +
                "}";
    }

    private String generateRoomPin() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }

    private String getServer() {
        // scale-out을 위해 각 미디어 서버별 사용량 체크 후 여유 있는 서버를 방에 부여
        // 일단 미디어 서버는 하나
        return server.get(0);
    }



}
