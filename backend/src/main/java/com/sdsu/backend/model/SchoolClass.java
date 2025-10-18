package com.sdsu.backend.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SCHOOL_CLASS")
public class SchoolClass extends CalendarEvent{
    @Override
    public int getPriority() {
        return 3;
    }
}
