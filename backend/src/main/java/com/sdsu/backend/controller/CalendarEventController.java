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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            // Find calendar by ID
            Optional<Calendar> calendarOpt = calendarService.findById(request.getCalendarId());

            if (calendarOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            CalendarEvent event = getCalendarEvent(request, calendarOpt);

            // Save to database
            CalendarEvent saved = calendarEventService.save(event);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    private static CalendarEvent getCalendarEvent(CreateEventRequest request, Optional<Calendar> calendarOpt) {
        Calendar calendar = calendarOpt.get();

        // CREATE THE CORRECT TYPE BASED ON eventType
        CalendarEvent event = null;
        String eventType = request.getEventType() != null ? request.getEventType() : "ASSIGNMENT";

        switch (eventType.toUpperCase()) {
            case "SCHOOL_CLASS" -> event = new SchoolClass();
            case "SPECIAL_EVENT" -> event = new SpecialEvent();
            case "ASSIGNMENT" -> event = new Assignment();
        }

        // Set common fields
        assert event != null;
        event.setDateFromString(request.getDate());
        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setTitle(request.getTitle());
        event.setCalendar(calendar);
        return event;
    }

    // ===== READ ALL =====
    @GetMapping
    public ResponseEntity<List<CalendarEvent>> getAllEventsByCalendar(@RequestParam Long calendarId) {
        try {
            String startDate = "2025-01-01";
            String endDate = "2025-12-31";
            List<CalendarEvent> events = calendarEventService.getByDateRangeEpoch(calendarId, startDate, endDate);

            return ResponseEntity.ok(events);

        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // ===== READ ONE =====
    @GetMapping("/{id}")
    public ResponseEntity<CalendarEvent> getEventById(@PathVariable Long id) {
        Optional<CalendarEvent> event = calendarEventService.findById(id);

        return event.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ===== UPDATE =====
    @PutMapping("/{id}")
    public ResponseEntity<CalendarEvent> updateCalendarEvent(@PathVariable Long id,
            @RequestBody UpdateEventRequest request) {
        try {
            Optional<CalendarEvent> eventOpt = calendarEventService.findById(id);

            if (eventOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            CalendarEvent event = eventOpt.get();

            // If eventType changed, we need to create a new object of the new type
            // For now, we'll just update the existing event's fields
            // (Changing type requires deleting old and creating new, which is complex)

            // Update fields if provided
            if (request.getDate() != null) {
                event.setDateFromString(request.getDate());
            }
            if (request.getStartTime() != null) {
                event.setStartTime(request.getStartTime());
            }
            if (request.getEndTime() != null) {
                event.setEndTime(request.getEndTime());
            }
            if (request.getTitle() != null) {
                event.setTitle(request.getTitle());
            }

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