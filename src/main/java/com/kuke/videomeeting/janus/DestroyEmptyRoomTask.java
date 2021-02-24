package com.kuke.videomeeting.janus;

import com.google.gson.Gson;
import com.kuke.videomeeting.model.dto.room.RoomDto;
import com.kuke.videomeeting.model.dto.room.RoomListResultDto;
import com.kuke.videomeeting.service.room.RoomService;
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

    private final RoomService roomService;
    private final Set<String> prevEmptyRooms; // 이전 검사에서 비어있다고 나온 방들
    private final long detectEmptyRoomPeriod = 1000L * 60; // 해당 주기마다 빈 방 검사

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(detectEmptyRoomPeriod);

                List<RoomDto> list = roomService.readAllRooms();
                Set<String> emptyRooms = list.stream().filter(i -> i.getNum_participants() == 0)
                        .map(i -> i.getRoom()).collect(Collectors.toSet()); // 빈 방 목록

                List<String> destroyRoomList = emptyRooms.stream() // 이전 검사도 비어있어서 파괴해야할 방 목록
                        .filter(i -> prevEmptyRooms.contains(i)).collect(Collectors.toList());

                destroyRoomList.stream().forEach(i -> {
                    roomService.destroyRoom(i);
                    emptyRooms.remove(i); // 이미 제거한 빈방
                });

                prevEmptyRooms.clear();
                prevEmptyRooms.addAll(emptyRooms);
            } catch (Exception e) {
                // continue
            }
        }
    }

}
