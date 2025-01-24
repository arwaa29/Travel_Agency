package org.example.travelagency.eventBooking.Repository;

import org.example.travelagency.eventBooking.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Custom query to find events by hotel ID (optional)
    // You can add more custom methods as needed
    List<Event> findByHotelId(Long hotelId);

    // You can also create methods for booking or cancelling an event
    // For example, if you want to decrement available tickets for an event, you can create a custom method.
}
