package org.example.travelagency.hotelBooking.Service;

import org.example.travelagency.hotelBooking.Model.Room;
import org.example.travelagency.hotelBooking.Model.Booking;
import org.example.travelagency.userManagement.User;
import org.example.travelagency.userManagement.UserRepository;
import org.example.travelagency.hotelBooking.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private BookingRepository bookingRepository;

    // Fetch all bookings of a user by userId
    public List<Booking> getUserBookings(Long userId) {
        // Use the findByUserId method from BookingRepository to fetch bookings
        return bookingRepository.findByUserId(userId);
    }

    // Book a room for a user with check-in and check-out dates
    public String bookRoomForUser(Long userId, Long roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        Optional<User> user = userRepository.findById(userId);

        // Check if the user exists and room is available for booking
        if (user.isPresent() && roomService.checkRoomAvailability(roomId)) {
            Room bookedRoom = roomService.markRoomAsBooked(roomId); // Mark room as booked
            if (bookedRoom != null) {
                // Create and save the booking
                Booking booking = new Booking();
                booking.setUser(user.get());
                booking.setRoom(bookedRoom);
                booking.setCheckInDate(checkInDate);  // Set check-in date
                booking.setCheckOutDate(checkOutDate);  // Set check-out date
                booking.setCancelled(false);  // Set isCancelled to false by default

                // Save the booking to the database
                bookingRepository.save(booking);

                return "Booking done for " + user.get().getUsername();  // Return confirmation message
            }
        }
        return "Room is not available or user not found";  // Error message if room unavailable or user not found
    }

    // Method to calculate the duration of stay in days
    public long calculateStayDuration(Long bookingId) {
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            LocalDate checkInDate = booking.getCheckInDate();
            LocalDate checkOutDate = booking.getCheckOutDate();

            // Calculate the duration between check-in and check-out dates
            return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
        return 0;  // Return 0 if the booking doesn't exist
    }

    // Cancel a booking for a user
    public boolean cancelBookingForUser(Long userId, Long roomId) {
        Optional<User> user = userRepository.findById(userId);

        // Check if the user exists
        if (user.isPresent()) {
            // Find the booking associated with the room and user
            Optional<Booking> booking = user.get().getBookings().stream()
                    .filter(b -> b.getRoom().getId().equals(roomId))
                    .findFirst();

            // If booking exists, cancel it
            if (booking.isPresent()) {
                Booking canceledBooking = booking.get();
                roomService.markRoomAsAvailable(roomId);  // Mark room as available (canceled)
                user.get().getBookings().remove(canceledBooking);  // Remove booking from user's list
                bookingRepository.delete(canceledBooking);  // Delete the booking from the database
                userRepository.save(user.get());  // Save the updated user

                return true; // Return true to indicate successful cancellation
            }
        }
        return false; // Return false if user or booking is not found
    }
}
