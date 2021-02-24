package com.kuke.videomeeting.janus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TaskManager {
    private final DestroyEmptyRoomTask destroyEmptyRoomTask;

    @PostConstruct
    public void startTask() {
        new Thread(destroyEmptyRoomTask).start();
    }
}
