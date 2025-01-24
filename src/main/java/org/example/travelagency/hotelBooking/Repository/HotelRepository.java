package org.example.travelagency.hotelBooking.Repository;

import org.example.travelagency.hotelBooking.Model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Method to find hotels by location
    List<Hotel> findByLocation(String location);

}
