package org.example.travelagency.eventBooking.Model;


import jakarta.persistence.*;
import org.example.travelagency.hotelBooking.Model.Hotel;  // Importing the Hotel class

import java.util.Date;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private Date eventDate;
    private int availableTickets;

    // Hotel foreign key
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    // Constructors
    public Event() {}

    public Event(String name, String location, Date eventDate, int availableTickets, Hotel hotel) {
        this.name = name;
        this.location = location;
        this.eventDate = eventDate;
        this.availableTickets = availableTickets;
        this.hotel = hotel;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void decrementAvailableTickets() {
        this.availableTickets--;
    }
}

