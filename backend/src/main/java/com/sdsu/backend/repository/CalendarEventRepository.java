package com.sdsu.backend.repository;

import com.sdsu.backend.model.CalendarEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface CalendarEventRepository extends JpaRepository <CalendarEvent, Long>{

    List<CalendarEvent> findByCalendarIdAndEpochDate(Long calendarId, long epochDate); //returns all caldendar items at a specific day
}
