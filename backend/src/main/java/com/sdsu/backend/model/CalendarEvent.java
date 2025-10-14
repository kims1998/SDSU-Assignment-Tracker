package com.sdsu.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter; //used for formatting and julianDate calculations

@Entity
public class CalendarEvent{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String date;
    private long julianDate;
    private String startTime;
    private String endTime;
    private String title;


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyy-MM-dd");
    private static final long JULIAN_EPOCH_OFFSET = 2440588L; //converts epoch time to Julian and vice versa

    public CalendarEvent(){}

    public CalendarEvent(String date, String startTime, String endTime, String title) {
        setDateFromString(date);
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
    }

    // start getters/setters
    public Long getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = Id;
    }

        //overlap with date setters ensures that whenever one is changed, both are changed
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

    // end getters/setters

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