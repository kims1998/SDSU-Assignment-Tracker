package com.sdsu.backend.service;

import com.sdsu.backend.model.CalendarEvent;
import com.sdsu.backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarEventService {
    private final CalendarEventRepository calEvRepo;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public CalendarEventService(CalendarEventRepository calEvRepo){
        this.calEvRepo = calEvRepo;
    }

    public CalendarEvent save(CalendarEvent calendarEvent){
        if (calendarEvent == null){
            throw new IllegalArgumentException("CalendarEvent must not be null");
        }
        if (calendarEvent.getCalendar() == null){
            throw new IllegalArgumentException("CalendarEvent must be associated with Calendar");
        }
        if (calendarEvent.getTitle() == null || calendarEvent.getTitle().isEmpty()){
            throw new IllegalArgumentException("CalendarEvent must have a valid title");
        }
        return calEvRepo.save(calendarEvent);
    }

    public Optional<CalendarEvent> getByName(Long calendarId, String title){
        return calEvRepo.findByCalendarIdAndTitle(calendarId,title);
    }

    public List<CalendarEvent> getByEpochDate(Long calendarId, long epochDate){
        return calEvRepo.findByCalendarIdAndEpochDate(calendarId, epochDate);
    }

    public List<CalendarEvent> getByStringDate(Long calendarId, String dateStr){
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr, FORMATTER);
        }
        catch (DateTimeParseException e){
            throw new IllegalArgumentException("Invalid date format. Expect yyyy-MM-dd");
        }
        long longDate = date.toEpochDay();                                      //you can use .toEpochDay() elsewhere to find start for inputs btw
        return calEvRepo.findByCalendarIdAndEpochDate(calendarId, longDate);    //converts to long, then passes through epochdate
    }

    public List<CalendarEvent> getByDateRangeEpoch(Long calendarId, long dateStart, long dateEnd){ //use epochdate to epochdate+6 for a week
        return calEvRepo.findByCalendarIdAndEpochDateBetween(calendarId, dateStart, dateEnd);
    }

    public List<CalendarEvent> getByDateRangeEpoch(Long calendarId, String dateStart, String dateStop){
        LocalDate dateBegin,
                  dateEnd;
        try{
            dateBegin = LocalDate.parse(dateStart, FORMATTER);
            dateEnd = LocalDate.parse(dateStop, FORMATTER);
        }
        catch (DateTimeParseException e){
            throw new IllegalArgumentException("Invalid date format. Expect yyyy-MM-dd");
        }
        long begin_num = dateBegin.toEpochDay();
        long end_num = dateEnd.toEpochDay();
        return calEvRepo.findByCalendarIdAndEpochDateBetween(calendarId, begin_num, end_num);
    }

    public void deleteById(Long id){
        calEvRepo.deleteById(id);
    }

    public Optional<CalendarEvent> findById (Long id){
        return calEvRepo.findById(id);
    }
}
