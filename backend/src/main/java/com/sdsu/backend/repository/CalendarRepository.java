package com.sdsu.backend.repository;

import com.sdsu.backend.model.Calendar;
import com.sdsu.backend.model.CalendarEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository <Calendar,Long> {
}
