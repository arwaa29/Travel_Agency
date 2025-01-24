package org.example.travelagency.hotelBooking.Repository;

import org.example.travelagency.hotelBooking.Model.Hotel;
import org.example.travelagency.hotelBooking.Model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // Custom query to find rooms by type and hotel
    List<Room> findByRoomTypeAndHotel(String roomType, Hotel hotel);

    // Other query methods you may have:

  //List<Room> findByHotelId(Long hotelId);
  //List<Room> findByAvailableTrue();

}
