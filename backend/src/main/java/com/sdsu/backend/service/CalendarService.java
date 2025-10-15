package com.sdsu.backend.service;

import com.sdsu.backend.repository.CalendarRepository;
import com.sdsu.backend.model.Calendar;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {
    private final CalendarRepository calRepo;

    public CalendarService(CalendarRepository calRepo){
        this.calRepo = calRepo;
    }

    public Calendar save(Calendar cal){
        return calRepo.save(cal);
    }

    public void deleteById(long id){
        calRepo.deleteById(id);
    }

}
