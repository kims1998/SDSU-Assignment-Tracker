package com.sdsu.backend.service;

import com.sdsu.backend.repository.CalendarRepository;
import com.sdsu.backend.model.Calendar;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calRepo;

    public CalendarService(CalendarRepository calRepo){
        this.calRepo = calRepo;
    }

    public Calendar save(Calendar cal){
        return calRepo.save(cal);
    }
    public void deleteById(Long id){
        calRepo.deleteById(id);
    }

    public Optional<Calendar> findById(Long id){ return calRepo.findById(id);}

}
