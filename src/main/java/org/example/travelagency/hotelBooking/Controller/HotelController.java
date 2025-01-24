package org.example.travelagency.hotelBooking.Controller;

import org.example.travelagency.hotelBooking.Model.Hotel;
import org.example.travelagency.hotelBooking.Service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    // Get all hotels (fetch from external mock API and store in internal DB)
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        try {
            List<Hotel> hotels = hotelService.getAllHotels();
            return new ResponseEntity<>(hotels, HttpStatus.OK);
        } catch (Exception e) {
            // If any error occurs, return a 500 Internal Server Error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get hotel by ID (fetch from internal DB)
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);

        if (hotel == null) {
            // If no hotel is found, return 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(hotel, HttpStatus.OK);
    }

    // Add a new hotel (manually for admin use)
    @PostMapping
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        try {
            Hotel addedHotel = hotelService.addHotel(hotel);
            return new ResponseEntity<>(addedHotel, HttpStatus.CREATED);
        } catch (Exception e) {
            // If any error occurs, return a 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update a hotel by ID
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
        Hotel updated = hotelService.updateHotel(id, updatedHotel);

        if (updated == null) {
            // If hotel is not found or could not be updated, return 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete a hotel by ID (manually for admin use)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        try {
            hotelService.deleteHotel(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Success response without content
        } catch (Exception e) {
            // If the hotel cannot be found or deleted, return 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
