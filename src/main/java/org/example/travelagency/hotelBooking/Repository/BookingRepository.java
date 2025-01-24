package org.example.travelagency.hotelBooking.Repository;

import org.example.travelagency.hotelBooking.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    // Find bookings by user
    List<Booking> findByUserId(Long userId);



}
