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

    //@OneToOne ---> remember to do this tomorrow
    //private User user
    private String date;
    private long julianDate;               // can use this for quick searching
    private String startTime;
    private String endTime;
    private String title;
    private int priority;                   //use for print order logic or whatever

    @ManyToOne                              //link to calendar entity
    @JoinColumn(name = "calendar_id")
    private Calendar calendar;

    @Transient
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Transient //prevents jakarta keeping these up
    private static final long JULIAN_EPOCH_OFFSET = 2440588L; //converts epoch time to Julian and vice versa

    public CalendarEvent(){}

    public CalendarEvent(String date, String startTime, String endTime, String title, Calendar calendar) {
        setDateFromString(date);
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
        this.julianDate = toJulian(dateStr);
    }

    public void setDateFromJul(long dateJul){ //takes julian date input, sets both dates
        this.julianDate = dateJul;
        this.date = fromJulian(dateJul);
    }

    public String getDate() {
        return date;
    }

    public Long getJulianDate() {
        return julianDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public abstract int getPriority();

    // end getters/setters

    // vvv Util methods to convert back and forth to and from Julian

    private  long toJulian (String dateStr){
        LocalDate date = LocalDate.parse(dateStr, FORMATTER);
        return date.toEpochDay() + JULIAN_EPOCH_OFFSET;
    }
    private String fromJulian (long julDate){
        long epochDay = julDate - JULIAN_EPOCH_OFFSET;
        LocalDate date = LocalDate.ofEpochDay(epochDay);
        return date.format(FORMATTER);
    }
}