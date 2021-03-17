package com.kuke.videomeeting.model.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessionResultDto {
    private String janus;
    private String transaction;
    private List<String> sessions;
}
