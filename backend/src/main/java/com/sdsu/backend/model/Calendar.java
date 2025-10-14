package com.sdsu.backend.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CalendarEvent> calendarEvents = new HashSet<>();

    public Calendar (){}


    public Calendar(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public void addCalendarEvent(CalendarEvent cEvent){ //helper method ==> helps keep oneToMany/manyToOne
        this.getCalendarEvents().add(cEvent);
        cEvent.setCalendar(this);
    }

}


