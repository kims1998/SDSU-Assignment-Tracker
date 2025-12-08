package com.sdsu.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; /* 21OCT2025 Added this import due to Calendar and CalendarEvent
                                                              referencing each other when converting to JSON */
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter; //used for formatting and julianDate calculations

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties({ "calendar" }) //ignores calendar field in JSON
public abstract class CalendarEvent {
    // start getters/setters using Lombok (don't have to write the getter/setters)
    @Getter @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String title;

    //@Setter will have warning, but we need the custom setters below
    @Getter @Setter
    private LocalDateTime startDateTime;
    @Getter @Setter
    private LocalDateTime endDateTime;

    @Getter @Setter
    private long epochStart;

    @Getter @Setter
    private long epochEnd;

    @Getter @Setter
    private String eventType;

    @Getter @Setter
    private int priority; // use for print order logic or whatever

    // needed for helper
    @Getter @Setter @ManyToOne // link to calendar entity
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Transient
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public CalendarEvent() {
        //Default constructor required by JPA
    }

    //Constructor for humans
    public CalendarEvent(String startStr, String endStr, String title, Calendar calendar, String eventType, int priority) {
        this.startDateTime = LocalDateTime.parse(startStr, FORMATTER);
        this.endDateTime = LocalDateTime.parse(endStr, FORMATTER);
        this.title = title;
        this.calendar = calendar;
        this.eventType = eventType;
        this.priority = priority;
        updateEpochField();
    }

    //Constructor for machines
    public CalendarEvent(LocalDateTime start, LocalDateTime end, String title, Calendar calendar, String eventType, int priority) {
        this.startDateTime = start;
        this.endDateTime = end;
        this.title = title;
        this.calendar = calendar;
        this.eventType = eventType;
        this.priority = priority;
        updateEpochField();
    }

    public void updateEpochField() {
        ZoneId zone = ZoneId.systemDefault();
        if (startDateTime != null) {
            this.epochStart = startDateTime.atZone(zone).toInstant().toEpochMilli();
        }
        if (endDateTime != null) {
            this.epochEnd = endDateTime.atZone(zone).toInstant().toEpochMilli();
        }
    }

    public void setStartDateTime(LocalDateTime dt) {
        this.startDateTime = dt;
    }

    public void setEndDateTime(LocalDateTime dt) {
        this.endDateTime = dt;
    }
}