package com.sdsu.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Calendar {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CalendarEvent> calendarEvents = new HashSet<>();
    private boolean isShareable;

    public Calendar (){
        //Default constructor required by JPA
    }

    public Calendar (User user) {
        this.user = user;
        this.isShareable = false;
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