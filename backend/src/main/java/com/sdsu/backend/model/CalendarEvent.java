package com.sdsu.backend.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; //used for formatting and julianDate calculations

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CalendarEvent{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;
    private long epochDate;               // can use this for quick searching
    private double startTime;
    private double endTime;
    private String title;
    private int priority;                   //use for print order logic or whatever

    @ManyToOne                              //link to calendar entity
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Transient
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CalendarEvent(){} //mt constructor for Springy Boi

    public CalendarEvent(String date, double startTime, double endTime, String title, Calendar calendar) { //constructor for humans
        setDateFromString(date);
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.calendar = calendar;
    }

    public CalendarEvent(Long date, double startTime, double endTime, String title, Calendar calendar) { //constructor for machine
        setDateFromEpoch(date);
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.calendar = calendar;
    }

    // start getters/setters
    public Long getId() {
        return id;
    }


        //overlap with date setters ensures that whenever one is changed, both are changed. Prevents logic confusion
    public void setDateFromString(String dateStr){ //takes string input, sets both dates
        this.date = dateStr;
        this.epochDate = toEpoch(dateStr);
    }

    public void setDateFromEpoch(long epoch){ //takes julian date input, sets both dates
        this.epochDate = epoch;
        this.date = fromEpoch(epoch);
    }

    public String getDate() {
        return date;
    }

    public Long getEpochDate() {
        return epochDate;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCalendar(Calendar calendar){ // needed for helper
        this.calendar = calendar;
    }

    public Calendar getCalendar(){ return calendar;}

    public abstract int getPriority();

    // end getters/setters

    // vvv Util methods to convert back and forth to and from Julian

    private  long toEpoch(String dateStr){
        LocalDate date = LocalDate.parse(dateStr, FORMATTER);
        return date.toEpochDay();
    }
    private String fromEpoch(long epochIn){
        LocalDate date = LocalDate.ofEpochDay(epochIn);
        return date.format(FORMATTER);
    }
}