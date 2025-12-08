package com.sdsu.backend.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private Long calendarId;
    private String title;
    private String eventType;
    private String date;
    private String startTime;
    private String endTime;
}
