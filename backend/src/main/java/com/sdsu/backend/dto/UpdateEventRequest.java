package com.sdsu.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String title;
    private String eventType;
    private String date;
    private Double startTime;
    private Double endTime;
    private Double priority;
}