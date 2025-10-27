package com.sdsu.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    private Long calendarId;
    private String title,
                   eventType,
                   date;
    private Double startTime,
                   endTime;
}
