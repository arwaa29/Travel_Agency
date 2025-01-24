package org.example.travelagency.eventBooking.service;

import org.example.travelagency.eventBooking.Model.EventBooking;
import org.example.travelagency.eventBooking.Model.Event;
import org.example.travelagency.eventBooking.Repository.EventBookingRepository;
import org.example.travelagency.eventBooking.Repository.EventRepository;
import org.example.travelagency.userManagement.User;
import org.example.travelagency.userManagement.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventBookingService {

    private final EventBookingRepository eventBookingRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;  // Assuming you have a UserRepository

    @Autowired
    public EventBookingService(EventBookingRepository eventBookingRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.eventBookingRepository = eventBookingRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public String bookEvent(Long eventId, Long userId, Long hotelId) {
        // Fetch the event from the database
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isEmpty()) {
            return "Event with ID " + eventId + " not found.";
        }

        Event event = eventOptional.get();

        // Fetch the user from the database
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return "User with ID " + userId + " not found.";
        }

        User user = userOptional.get();

        // Check if tickets are available
        if (event.getAvailableTickets() <= 0) {
            return "No tickets available for this event.";
        }

        // Decrease the number of available tickets for the event
        event.decrementAvailableTickets();
        eventRepository.save(event);

        // Create a new EventBooking object
        EventBooking eventBooking = new EventBooking(user, event, event.getHotel());  // Assuming hotel comes from the event itself
        eventBookingRepository.save(eventBooking);

        // Return success message
        return "Ticket booked for " + user.getUsername();
    }
}
