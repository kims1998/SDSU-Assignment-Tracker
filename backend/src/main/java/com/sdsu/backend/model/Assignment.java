package com.sdsu.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ASSIGNMENT")
public class Assignment extends CalendarEvent{
    @Override
    public int getPriority() {
        return 2;
    }
}
