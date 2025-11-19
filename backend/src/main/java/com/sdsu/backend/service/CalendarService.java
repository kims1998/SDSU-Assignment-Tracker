package com.sdsu.backend.service;

import com.sdsu.backend.repository.CalendarRepository;
import com.sdsu.backend.model.Calendar;
import org.springframework.stereotype.Service;
// IMPORT SOLELY FOR TEST PURPOSE SINCE NOT LINKING CALENDAR TO A USER
// RUNS ONCE ON APP START AND CREATE A calednarId =1
// == REMOVE THIS LATER ON OF DEVELOPEMENT ==
import jakarta.annotation.PostConstruct;

import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calRepo;

    public CalendarService(CalendarRepository calRepo) {
        this.calRepo = calRepo;
    }

    // ==== REMOVE LATER =====
    @PostConstruct
    public void initializeDefaultCalendar() {
        if (calRepo.count() == 0) {
            Calendar defaultCalendar = new Calendar();
            defaultCalendar.setIsSharable(false);
            calRepo.save(defaultCalendar);
        }
    }

    public Calendar save(Calendar cal) {
        return calRepo.save(cal);
    }

    public void deleteById(Long id) {
        calRepo.deleteById(id);
    }

    public Optional<Calendar> findById(Long id) {
        return calRepo.findById(id);
    }
}
