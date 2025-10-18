package com.sdsu.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SPECIAL_EVENT")
public class SpecialEvent extends CalendarEvent{
    @Override
    public int getPriority() {
        return 1;
    }
}
