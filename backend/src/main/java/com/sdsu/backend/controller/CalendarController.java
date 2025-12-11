package com.sdsu.backend.controller;
import com.sdsu.backend.model.Calendar;
import com.sdsu.backend.service.CalendarService;
import com.sdsu.backend.dto.CreateCalendarRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    private static final Logger LOG = Logger.getLogger(CalendarController.class.getName());
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // POST http://localhost:8080/api/calendars
    @PostMapping
    public ResponseEntity<Calendar> createCalendar(@RequestBody CreateCalendarRequest request) {
        try {
            Calendar calendar = new Calendar();
            calendar.setIsSharable(request.isShareable());
            Calendar saved = calendarService.save(calendar);

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    // GET http://localhost:8080/api/calendars/1
    @GetMapping("/{id}")
    public ResponseEntity<Calendar> getCalendarById(@PathVariable Long id) {
        Optional<Calendar> calendar = calendarService.findById(id);

        return calendar.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE http://localhost:8080/api/calendars/1
    @DeleteMapping
    public ResponseEntity<Void> deleteCalendar(@PathVariable Long id) {
        try {
            calendarService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}