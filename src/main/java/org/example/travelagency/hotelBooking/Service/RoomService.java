package org.example.travelagency.hotelBooking.Service;

import org.example.travelagency.hotelBooking.Model.Room;
import org.example.travelagency.hotelBooking.Model.Hotel;
import org.example.travelagency.hotelBooking.Repository.RoomRepository;
import org.example.travelagency.hotelBooking.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String externalApiUrl = "https://4f6d45b1-e259-404d-a109-5e92726e0fb0.mock.pstmn.io/rooms";

    /**
     * Fetch all rooms from the database, or if no rooms exist, fetch them from an external API
     */
    @Transactional
    public List<Room> getAllRooms() {
        // Fetch rooms from the database first
        List<Room> roomsInDb = roomRepository.findAll();

        // If no rooms are found in the database, fetch from external API
        if (roomsInDb.isEmpty()) {
            Room[] roomsFromApi = restTemplate.getForObject(externalApiUrl, Room[].class);

            if (roomsFromApi != null) {
                // Process rooms from the API and associate with hotels
                for (Room room : roomsFromApi) {
                    normalizeRoomType(room);

                    // Link room to hotel based on hotel_id
                    if (room.getHotel() != null && room.getHotel().getId() != null) {
                        Optional<Hotel> hotel = hotelRepository.findById(room.getHotel().getId());
                        hotel.ifPresent(room::setHotel); // Link room to the found hotel
                    }
                }

                // Save rooms from API to the database
                roomRepository.saveAll(Arrays.asList(roomsFromApi));
            }
            return Arrays.asList(roomsFromApi); // Return the rooms fetched from API
        }

        return roomsInDb; // Return rooms from the database if available
    }

    // Method to find hotel by ID
    public Hotel findHotelById(Long hotelId) {
        return hotelRepository.findById(hotelId).orElse(null); // Returns the hotel if found, otherwise null
    }

    /**
     * Add a new room, ensuring it's linked to a hotel
     */
    public Room addRoom(Room room, Long hotelId) {
        // Fetch hotel by hotelId
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelId);
        if (hotelOptional.isEmpty()) {
            throw new IllegalArgumentException("Hotel with ID " + hotelId + " not found");
        }

        // Associate room with the fetched hotel
        room.setHotel(hotelOptional.get());

        // Normalize room type
        normalizeRoomType(room);

        // Save and return the room
        return roomRepository.save(room);
    }

    /**
     * Search rooms by type and hotel ID
     */
    public List<Room> searchRooms(String roomType, Long hotelId) {
        Optional<Hotel> hotel = hotelRepository.findById(hotelId);
        return hotel.map(h -> roomRepository.findByRoomTypeAndHotel(roomType, h)).orElse(null);
    }

    /**
     * Get room details by room ID
     */
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId).orElse(null); // Returns null if room not found
    }

    /**
     * Check room availability by room ID
     */
    public boolean checkRoomAvailability(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        return room.isPresent() && room.get().isAvailable(); // Check availability
    }

    /**
     * Mark the room as booked (unavailable)
     */
    public Room markRoomAsBooked(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        if (room.isPresent() && room.get().isAvailable()) {
            Room bookedRoom = room.get();
            bookedRoom.setAvailable(false); // Mark room as unavailable
            roomRepository.save(bookedRoom); // Save updated room
            return bookedRoom;
        }
        return null; // Return null if room not found or already booked
    }

    /**
     * Mark the room as available (on cancellation)
     */
    public Room markRoomAsAvailable(Long roomId) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent()) {
            Room room = roomOptional.get();
            room.setAvailable(true); // Mark room as available
            roomRepository.save(room); // Save updated room
            return room;
        }
        return null; // Return null if room not found
    }

    /**
     * Helper method to normalize and validate room types
     */
    private void normalizeRoomType(Room room) {
        if (room.getRoomType() == null || room.getRoomType().isEmpty()) {
            room.setRoomType("Unknown");  // Default value if missing
        } else {
            // Normalize room type to a standard format (capitalize first letter)
            String normalizedRoomType = room.getRoomType().trim().toLowerCase();
            if (normalizedRoomType.equals("single") || normalizedRoomType.equals("double") ||
                    normalizedRoomType.equals("family")) {
                room.setRoomType(Character.toUpperCase(normalizedRoomType.charAt(0)) + normalizedRoomType.substring(1));
            } else {
                room.setRoomType("Unknown");  // Set to Unknown if not a valid type
            }
        }
    }
}
