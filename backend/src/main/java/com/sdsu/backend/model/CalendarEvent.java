package com.sdsu.backend.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; /* 21OCT2025 Added this import due to Calendar and CalendarEvent
                                                              referencing each other when converting to JSON */
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; //used for formatting and julianDate calculations

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@JsonIgnoreProperties({ "calendar" }) //ignores calendar field in JSON
public abstract class CalendarEvent {
    // start getters/setters using Lombok (don't have to write the getter/setters)
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long epochDate; // can use this for quick searching
    @Getter
    private String date;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private double startTime;
    @Getter @Setter
    private double endTime;
    @Setter @Getter
    private String eventType;
    @Getter @Setter
    private int priority; // use for print order logic or whatever

    // needed for helper
    @Getter @Setter
    @ManyToOne // link to calendar entity
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Transient
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CalendarEvent() {
    } // mt constructor for Springy Boi

    //Constructor for humans
    public CalendarEvent(String date, double startTime, double endTime, String title, Calendar calendar) {
        setDateFromString(date);
        this.title = title;
        this.calendar = calendar;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //Constructor for machines
    public CalendarEvent(Long date, double startTime, double endTime, String title, Calendar calendar) {
        setDateFromEpoch(date);
        this.title = title;
        this.calendar = calendar;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // overlap with date setters ensures that whenever one is changed, both are
    // changed. Prevents logic confusion
    public void setDateFromString(String dateStr) { // takes string input, sets both dates
        this.date = dateStr;
        this.epochDate = toEpoch(dateStr);
    }

    public void setDateFromEpoch(long epoch) { // takes julian date input, sets both dates
        this.epochDate = epoch;
        this.date = fromEpoch(epoch);
    }

    public Long getEpochDate() {
        return epochDate;
    }

    // vvv Util methods to convert back and forth to and from Julian

    private long toEpoch(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, FORMATTER);
        return date.toEpochDay();
    }

    private String fromEpoch(long epochIn) {
        LocalDate date = LocalDate.ofEpochDay(epochIn);
        return date.format(FORMATTER);
    }
}