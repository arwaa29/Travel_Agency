package org.example.travelagency.hotelBooking.Service;

import org.example.travelagency.hotelBooking.Model.Hotel;
import org.example.travelagency.hotelBooking.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String externalApiUrl = "https://4f6d45b1-e259-404d-a109-5e92726e0fb0.mock.pstmn.io/hotels";

    // Fetch all hotels, save them to the database if not already present
    @Transactional
    public List<Hotel> getAllHotels() {
        // Fetch hotels from the database
        List<Hotel> hotelsInDb = hotelRepository.findAll();

        // If there are no hotels in the database, fetch from the external API
        if (hotelsInDb.isEmpty()) {
            Hotel[] hotelsFromApi = restTemplate.getForObject(externalApiUrl, Hotel[].class);

            if (hotelsFromApi != null && hotelsFromApi.length > 0) {
                hotelRepository.saveAll(Arrays.asList(hotelsFromApi));
                return Arrays.asList(hotelsFromApi); // Return fetched hotels
            } else {
                throw new RuntimeException("Failed to fetch hotels from external API");
            }
        }

        return hotelsInDb; // Return hotels from the database
    }

    // Fetch a hotel by ID
    @Transactional(readOnly = true)
    public Hotel getHotelById(Long id) {
        Optional<Hotel> hotelInDb = hotelRepository.findById(id);

        if (hotelInDb.isPresent()) {
            return hotelInDb.get();
        } else {
            // If hotel not found in DB, fetch from the external API
            String apiUrlWithId = externalApiUrl + "/" + id;
            Hotel hotelFromApi = restTemplate.getForObject(apiUrlWithId, Hotel.class);

            if (hotelFromApi != null) {
                hotelRepository.save(hotelFromApi);  // Save the new hotel fetched from API
                return hotelFromApi;
            } else {
                throw new RuntimeException("Hotel with ID " + id + " not found in external API");
            }
        }
    }

    // Add a new hotel
    @Transactional
    public Hotel addHotel(Hotel hotel) {
        if (hotel == null || hotel.getName() == null || hotel.getLocation() == null) {
            throw new IllegalArgumentException("Hotel data is invalid");
        }
        return hotelRepository.save(hotel);
    }

    // Update an existing hotel
    @Transactional
    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        if (updatedHotel == null || updatedHotel.getName() == null || updatedHotel.getLocation() == null) {
            throw new IllegalArgumentException("Hotel update data is invalid");
        }

        return hotelRepository.findById(id)
                .map(hotel -> {
                    hotel.setName(updatedHotel.getName());
                    hotel.setLocation(updatedHotel.getLocation());
                    // Save the updated hotel
                    return hotelRepository.save(hotel);
                })
                .orElseThrow(() -> new RuntimeException("Hotel with ID " + id + " not found"));
    }

    // Delete a hotel
    @Transactional
    public void deleteHotel(Long id) {
        Optional<Hotel> hotelInDb = hotelRepository.findById(id);
        if (hotelInDb.isPresent()) {
            hotelRepository.deleteById(id);
        } else {
            throw new RuntimeException("Hotel with ID " + id + " not found");
        }
    }
}
