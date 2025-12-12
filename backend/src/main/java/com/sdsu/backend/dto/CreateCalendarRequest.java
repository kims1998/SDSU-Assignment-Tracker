package com.sdsu.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalendarRequest {
    private boolean shareable;
}