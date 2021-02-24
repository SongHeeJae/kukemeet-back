package com.kuke.videomeeting.model.dto.janus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JanusVideoroomListResult {
    private String janus;
    private String transaction;
    private JanusVideoroomListResponse response;
}
