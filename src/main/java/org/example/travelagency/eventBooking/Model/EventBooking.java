package org.example.travelagency.eventBooking.Model;

import jakarta.persistence.*;
import org.example.travelagency.userManagement.User; // Importing the User class
import org.example.travelagency.hotelBooking.Model.Hotel; // Importing the Hotel class

@Entity
@Table(name = "event_bookings")
public class EventBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Foreign key referencing the User entity

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event; // Foreign key referencing the Event entity

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel; // Foreign key referencing the Hotel entity

    @Column(name = "is_cancelled", nullable = true)
    private Boolean isCancelled;

    // Constructors, getters, and setters

    public EventBooking() {
    }

    public EventBooking(User user, Event event, Hotel hotel) {
        this.user = user;
        this.event = event;
        this.hotel = hotel;
        this.isCancelled = false; // Default value for new bookings
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public String toString() {
        return "EventBooking{" +
                "id=" + id +
                ", user=" + user.getUsername() + // Displaying the username
                ", event=" + event.getName() +
                ", hotel=" + hotel.getName() +
                ", isCancelled=" + isCancelled +
                '}';
    }
}
