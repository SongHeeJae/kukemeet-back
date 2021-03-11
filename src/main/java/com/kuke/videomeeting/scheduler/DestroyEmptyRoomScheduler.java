package com.kuke.videomeeting.scheduler;

import com.kuke.videomeeting.model.dto.room.RoomDto;
import com.kuke.videomeeting.service.file.FileService;
import com.kuke.videomeeting.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DestroyEmptyRoomScheduler {
    private final RoomService roomService;
    private final FileService fileService;
    private final Set<String> prevEmptyRooms = new HashSet<>(); // 이전 검사에서 비어있다고 나온 방들

    @Scheduled(fixedDelay = 1000L * 60)
    public void destroyEmptyRoom() {
        try {
            Set<String> emptyRooms = getEmptyRooms();
            List<String> destroyRooms = getDestroyRooms(emptyRooms);
            destroyRooms(emptyRooms, destroyRooms);
            prevEmptyRooms.clear();
            prevEmptyRooms.addAll(emptyRooms);
        } catch (Exception e) {
            // empty
        }
    }

    private Set<String> getEmptyRooms() throws Exception {
        return roomService.readAllRooms().stream()
                .filter(i -> i.getNum_participants() == 0)
                .map(i -> i.getRoom()).collect(Collectors.toSet());
    }

    private List<String> getDestroyRooms(Set<String> emptyRooms) { // 이전 검사도 비어있어서 파괴해야할 방 목록
        return emptyRooms.stream()
                .filter(i -> prevEmptyRooms.contains(i)).collect(Collectors.toList());
    }

    private void destroyRooms(Set<String> emptyRooms, List<String> destroyRooms) throws Exception{
        destroyRooms.stream().forEach(i -> {
            roomService.destroyRoom(i);
            fileService.deleteFilesInDirectory(i);
            emptyRooms.remove(i); // 이미 제거한 빈방
        });
    }

}
