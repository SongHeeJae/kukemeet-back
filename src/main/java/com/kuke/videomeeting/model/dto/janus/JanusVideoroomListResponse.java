package com.kuke.videomeeting.model.dto.janus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JanusVideoroomListResponse {
    private String videoroom;
    private List<JanusVideoroomInfo> list;
}
