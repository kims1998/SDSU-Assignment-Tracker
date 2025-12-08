package com.sdsu.backend.controller;

import com.sdsu.backend.model.CalendarEvent;
import com.sdsu.backend.model.Calendar;
import com.sdsu.backend.model.Assignment;
import com.sdsu.backend.model.SchoolClass;
import com.sdsu.backend.model.SpecialEvent;
import com.sdsu.backend.service.CalendarEventService;
import com.sdsu.backend.service.CalendarService;
import com.sdsu.backend.dto.CreateEventRequest;
import com.sdsu.backend.dto.UpdateEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    private static final Logger LOG = Logger.getLogger(CalendarEventController.class.getName());
    private final CalendarEventService calendarEventService;
    private final CalendarService calendarService;

    public CalendarEventController(CalendarEventService calendarEventService, CalendarService calendarService) {
        this.calendarEventService = calendarEventService;
        this.calendarService = calendarService;
    }

    // ===== CREATE =====
    @PostMapping
    public ResponseEntity<CalendarEvent> createCalendarEvent(@RequestBody CreateEventRequest request) {
        LOG.info("Received request: " + request);
        try {
            CalendarEvent event = buildCalendarEventFromRequest(request);
            CalendarEvent saved = calendarEventService.save(event);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    private CalendarEvent buildCalendarEventFromRequest(CreateEventRequest request) {
        // Find calendar by ID
        Optional<Calendar> calendarOpt = calendarService.findById(request.getCalendarId());
        if (calendarOpt.isEmpty()) throw new IllegalArgumentException("Calendar not found");

        Calendar calendar = calendarOpt.get();

        CalendarEvent event;
        String eventType = request.getEventType() != null ? request.getEventType() : "ASSIGNMENT";

        switch (eventType.toUpperCase()) {
            case "SCHOOL_CLASS" -> event = new SchoolClass();
            case "SPECIAL_EVENT" -> event = new SpecialEvent();
            case "ASSIGNMENT" -> event = new Assignment();
            default -> throw new IllegalArgumentException("Unknown event tpye: " + eventType);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(request.getDate() + "T" + request.getStartTime(), formatter);
        LocalDateTime end = LocalDateTime.parse(request.getDate() + "T" + request.getEndTime(), formatter);

        event.setStartDateTime(start);
        event.setEndDateTime(end);
        event.setTitle(request.getTitle());
        event.setEventType(request.getEventType());
        event.setCalendar(calendar);

        //Priority Mapping
        switch (eventType.toUpperCase()) {
            case "SCHOOL_CLASS" -> event.setPriority(3);
            case "SPECIAL_EVENT" -> event.setPriority(1);
            case "ASSIGNMENT" -> event.setPriority(2);
            default -> event.setPriority(99); // unknown/default
        }
        return event;
    }

    // ===== READ EVENTS BY CALENDAR & DATE RANGE =====
    @GetMapping
    public ResponseEntity<List<CalendarEvent>> getAllEventsByCalendarAndDate(
            @RequestParam Long calendarId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
            ) {
        try {
            List<CalendarEvent> events;

            if (date != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDateTime start = LocalDateTime.parse(date + "T00:00");
                LocalDateTime end = LocalDateTime.parse(date + "T23:59");

                events = calendarEventService.getByEpochRange(
                        calendarId,
                        start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                );
            } else if (startDate != null && endDate != null) {
                LocalDateTime start = LocalDateTime.parse(startDate + "T00:00");
                LocalDateTime end = LocalDateTime.parse(endDate + "T23:59");

                events = calendarEventService.getByEpochRange(
                        calendarId,
                        start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                        end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                );
            } else {
                events = calendarEventService.getAllByCalendar(calendarId);
            }
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== READ ONE =====
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEvent> getEventById(@PathVariable Long id) {
        return calendarEventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEvent> updateCalendarEvent(
            @PathVariable Long id,
            @RequestBody UpdateEventRequest request) {
        try {
            Optional<CalendarEvent> eventOpt = calendarEventService.findById(id);
            if (eventOpt.isEmpty()) return ResponseEntity.notFound().build();

            CalendarEvent event = eventOpt.get();
            calendarEventService.updateEventFromRequest(event, request);

            CalendarEvent updated = calendarEventService.save(event);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendarEvent(@PathVariable Long id) {
        try {
            calendarEventService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}