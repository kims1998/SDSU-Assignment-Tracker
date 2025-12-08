package com.sdsu.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    private String title;
    private String eventType;
    private String date;

    @Getter @Setter
    private String startTime;
    @Getter @Setter
    private String endTime;

    private Double priority;


}