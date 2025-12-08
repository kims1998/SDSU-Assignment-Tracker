package com.sdsu.backend.repository;

import com.sdsu.backend.model.CalendarEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarEventRepository extends JpaRepository <CalendarEvent, Long>{
    //Finds events fully or partially inside a range
    @Query("""
        SELECT e FROM CalendarEvent e
        WHERE e.calendar.id = :calendarId
        AND e.epochStart >= :epoch
        AND e.epochEnd <= :epoch
   """)

    List<CalendarEvent> findEventsOnDay(Long calendarId, long epoch);

    Optional<CalendarEvent> findByCalendarIdAndTitle (Long calendarId, String title);

    //Finds events fully or partially inside a range
    @Query("""
        SELECT e FROM CalendarEvent e
        WHERE e.calendar.id = :calendarId
        AND e.epochEnd >= :startEpoch
        AND e.epochStart <= :endEpoch
       """)
    List<CalendarEvent> findEventsInRange (Long calendarId, long startEpoch, long endEpoch); //returns all items within date range
}
