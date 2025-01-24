package org.example.travelagency.eventBooking.Repository;

import org.example.travelagency.eventBooking.Model.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventBookingRepository extends JpaRepository<EventBooking, Long> {

    // Find all bookings by user_id
    List<EventBooking> findByUserId(Long userId);

    // Find all bookings by event_id
    List<EventBooking> findByEventId(Long eventId);

    // Find all bookings by hotel_id
    List<EventBooking> findByHotelId(Long hotelId);

    // Find booking by user_id and event_id (to check if the user has already booked the event)
    EventBooking findByUserIdAndEventId(Long userId, Long eventId);

    // Find bookings that are not cancelled
    List<EventBooking> findByIsCancelledFalse();

    // Find bookings that are cancelled
    List<EventBooking> findByIsCancelledTrue();
}
