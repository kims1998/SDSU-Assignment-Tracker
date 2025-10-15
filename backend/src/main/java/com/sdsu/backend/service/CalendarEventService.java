package com.sdsu.backend.service;

import com.sdsu.backend.model.CalendarEvent;
import com.sdsu.backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarEventService {
    private final CalendarEventRepository calEvRepo;

    public CalendarEventService(CalendarEventRepository calEvRepo){
        this.calEvRepo = calEvRepo;
    }

    public List<CalendarEvent> getByEpochDate(Long calendarID, long epochDate){
        return calEvRepo.findByCalendarIdAndEpochDate(calendarID, epochDate);
    }

    public CalendarEvent save(CalendarEvent event){
        return calEvRepo.save(event);
    }

    public void deleteById(long id){
        calEvRepo.deleteById(id);
    }

    //create methods to make new CalendarEvents
    //ensure that method to create SchoolClass events iterates upon epochDate
    //make method that returns List of all CalendarEvents on the same epochDate
    //make method that returns ordered List of all calendarDays from 0 to 7 [i.e week]

}
