package org.example.travelagency.eventBooking.controller;

import org.example.travelagency.eventBooking.service.EventBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventBookingController {

    private final EventBookingService eventBookingService;

    @Autowired
    public EventBookingController(EventBookingService eventBookingService) {
        this.eventBookingService = eventBookingService;
    }

    // Endpoint to book an event ticket
    @PostMapping("/book")
    public ResponseEntity<String> bookEvent(@RequestParam Long eventId,
                                            @RequestParam Long userId,
                                            @RequestParam Long hotelId) {
        // Call the service to book the event
        String responseMessage = eventBookingService.bookEvent(eventId, userId, hotelId);

        // Return response based on the outcome
        if (responseMessage.contains("Ticket booked")) {
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.badRequest().body(responseMessage);
        }
    }
}
