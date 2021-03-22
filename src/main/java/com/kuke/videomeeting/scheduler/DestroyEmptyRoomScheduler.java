package com.kuke.videomeeting.scheduler;

import com.kuke.videomeeting.model.dto.room.RoomDto;
import com.kuke.videomeeting.service.file.FileService;
import com.kuke.videomeeting.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestroyEmptyRoomScheduler {
    private final RoomService roomService;
    private final Map<String, Set<String>> prevEmptyRooms = new HashMap<>(); // 이전 검사에서 비어있다고 나온 방들

    @Scheduled(fixedDelay = 1000L * 300)
    public void destroyEmptyRoom() {
        try {
            Map<String, Set<String>> emptyRooms = getEmptyRooms();
            Map<String, List<String>> destroyRooms = getDestroyRooms(emptyRooms);
            destroyRooms(emptyRooms, destroyRooms);
            prevEmptyRooms.clear();
            for (String s : emptyRooms.keySet()) {
                prevEmptyRooms.put(s, emptyRooms.get(s));
            }
        } catch (Exception e) {
            // empty
        }
    }

    private Map<String, Set<String>> getEmptyRooms() throws Exception {
        Map<String, List<RoomDto>> allRooms = roomService.readAllRooms();
        Map<String, Set<String>> result = new HashMap<>();
        for (String s : allRooms.keySet()) {
            List<RoomDto> rooms = allRooms.get(s);
            result.put(s, rooms.stream()
                    .filter(i -> i.getNum_participants() == 0)
                    .map(i -> i.getRoom())
                    .collect(Collectors.toSet()));
        }
        return result;
    }

    private Map<String, List<String>> getDestroyRooms(Map<String, Set<String>> emptyRooms) { // 이전 검사도 비어있어서 파괴해야할 방 목록
        Map<String, List<String>> result = new HashMap<>();
        for (String s : emptyRooms.keySet()) {
            Set<String> emptyRoom = emptyRooms.get(s);
            if(!prevEmptyRooms.containsKey(s)) continue;
            Set<String> prevEmptyRoom = prevEmptyRooms.get(s);
            result.put(s, emptyRoom.stream()
                    .filter(i -> prevEmptyRoom.contains(i)).collect(Collectors.toList()));
        }
        return result;
    }

    private void destroyRooms(Map<String, Set<String>> emptyRooms, Map<String, List<String>> destroyRooms) {
        for (String s : destroyRooms.keySet()) {
            List<String> destroyRoom = destroyRooms.get(s);
            Set<String> emptyRoom = emptyRooms.get(s);
            destroyRoom.stream().forEach(i -> {
                try {
                    roomService.destroyRoom(i, s);
                    emptyRoom.remove(i); // 이미 제거한 빈방
                } catch (Exception e) {
                    // continue;
                }
            });
        }
    }

}
