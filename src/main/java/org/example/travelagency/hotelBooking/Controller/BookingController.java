package org.example.travelagency.hotelBooking.Controller;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

import org.example.travelagency.hotelBooking.Model.Booking;
import org.example.travelagency.hotelBooking.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Fetch all bookings of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        if (bookings != null && !bookings.isEmpty()) {
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // No bookings found for user
        }
    }

    // Book a room for a user
    @PostMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<String> bookRoomForUser(
            @PathVariable Long userId,
            @PathVariable Long roomId,
            @RequestParam("checkInDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam("checkOutDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate) {

        String responseMessage = bookingService.bookRoomForUser(userId, roomId, checkInDate, checkOutDate);
        if (responseMessage.startsWith("Booking done")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.CREATED); // Booking successful
        } else {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST); // Room not available or user not found
        }
    }

    // Endpoint to get the duration of a stay by booking ID
    @GetMapping("/{bookingId}/duration")
    public long getStayDuration(@PathVariable Long bookingId) {
        return bookingService.calculateStayDuration(bookingId);
    }

    // Cancel a booking for a user
    @DeleteMapping("/user/{userId}/room/{roomId}")
    public ResponseEntity<Void> cancelBookingForUser(@PathVariable Long userId, @PathVariable Long roomId) {
        boolean isCanceled = bookingService.cancelBookingForUser(userId, roomId);
        if (isCanceled) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Booking canceled successfully
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // User or booking not found
        }
    }
}
