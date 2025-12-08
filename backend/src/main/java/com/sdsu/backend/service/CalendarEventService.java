package com.sdsu.backend.service;

import com.sdsu.backend.dto.UpdateEventRequest;
import com.sdsu.backend.model.CalendarEvent;
import com.sdsu.backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarEventService {
    private final CalendarEventRepository calEvRepo;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public CalendarEventService(CalendarEventRepository calEvRepo) {
        this.calEvRepo = calEvRepo;
    }

    // ===== CREATE / UPDATE =====
    public CalendarEvent save(CalendarEvent calendarEvent) {
        if (calendarEvent == null) {
            throw new IllegalArgumentException("CalendarEvent must not be null");
        }
        if (calendarEvent.getCalendar() == null) {
            throw new IllegalArgumentException("CalendarEvent must be associated with Calendar");
        }
        if (calendarEvent.getTitle() == null || calendarEvent.getTitle().isEmpty()) {
            throw new IllegalArgumentException("CalendarEvent must have a valid title");
        }
        calendarEvent.updateEpochField();
        return calEvRepo.save(calendarEvent);
    }

    public Optional<CalendarEvent> findById(Long id) {
        return calEvRepo.findById(id);
    }

    public void deleteById(Long id) {
        calEvRepo.deleteById(id);
    }

    public List<CalendarEvent> getAllByCalendar(Long calendarId) {
        return calEvRepo.findEventsInRange(calendarId, 0L, Long.MAX_VALUE);
    }

    public List<CalendarEvent> getByEpochRange(Long calendarId, long startEpoch, long endEpoch) {
        return calEvRepo.findEventsInRange(calendarId, startEpoch, endEpoch);
    }

    public void updateEventFromRequest(CalendarEvent event, UpdateEventRequest request) {
        try {
            if (request.getDate() != null) {
                LocalDateTime oldStart = event.getStartDateTime();
                LocalDateTime oldEnd = event.getEndDateTime();
                LocalDateTime newStart = LocalDateTime.parse(request.getDate() + "T" + oldStart.toLocalTime().toString(), DATE_TIME_FORMATTER);
                LocalDateTime newEnd = LocalDateTime.parse(request.getDate() + "T" + oldEnd.toLocalTime().toString(), DATE_TIME_FORMATTER);
                event.setStartDateTime(newStart);
                event.setEndDateTime(newEnd);
            }

            if (request.getStartTime() != null) {
                LocalDateTime newStart = LocalDateTime.parse(
                        event.getStartDateTime().toLocalDate() + "T" + request.getStartTime(), DATE_TIME_FORMATTER
                );
                event.setStartDateTime(newStart);
            }

            if (request.getEndTime() != null) {
                LocalDateTime newEnd = LocalDateTime.parse(
                        event.getEndDateTime().toLocalDate() + "T" + request.getEndTime(), DATE_TIME_FORMATTER
                );
                event.setEndDateTime(newEnd);
            }

            if (request.getTitle() != null) event.setTitle(request.getTitle());
            if (request.getEventType() != null) event.setEventType(request.getEventType());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date/time format");
        }
    }
}