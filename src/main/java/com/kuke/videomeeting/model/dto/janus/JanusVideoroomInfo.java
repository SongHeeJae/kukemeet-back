package com.kuke.videomeeting.model.dto.janus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JanusVideoroomInfo {
    private String room;
    private String description;
    private String pin_required;
    private String max_publishers;
    private String bitrate;
    private String fir_freq;
    private String require_pvtid;
    private String require_e2ee;
    private String notify_joining;
    private String audiocodec;
    private String videocodec;
    private String video_svc;
    private String record;
    private String lock_record;
    private int num_participants;
    private String audiolevel_ext;
    private String audiolevel_event;
    private String videoorient_ext;
    private String playoutdelay_ext;
    private String transport_wide_cc_ext;
}
