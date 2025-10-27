package com.sdsu.backend.model;

import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // once user class is finished do the following:
    //@OneToOne
    //@JoinColumn(name = "user_id")
    // private User user;
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CalendarEvent> calendarEvents = new HashSet<>();
    private boolean isShareable;

    public Calendar (){}

    //CODE FOR AFTER USER HERE vvv
    //public Calendar (User user){
    // create blank calendarEvents
    // set isSharable to false
    // Id is covered by @Id
    //}

    public Long getId() {
        return id;
    }

    public Set<CalendarEvent> getCalendarEvents() {
        return calendarEvents;
    }

    public boolean getIsSharable (){
        return isShareable;
    }

    public void setIsSharable(boolean share){
        isShareable = share;
    }

    public void addCalendarEvent(CalendarEvent cEvent){ //helper method ==> helps keep oneToMany/manyToOne
        this.getCalendarEvents().add(cEvent);
        cEvent.setCalendar(this);
    }

}