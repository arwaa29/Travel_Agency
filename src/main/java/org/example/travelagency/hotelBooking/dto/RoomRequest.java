package org.example.travelagency.hotelBooking.dto;

public class RoomRequest {
    private String room_type;
    private boolean available;
    private Long hotel_id;

    // Getters and Setters for the fields
    public String getRoomType() {
        return room_type;
    }

    public void setRoomType(String room_type) {
        this.room_type = room_type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Long getHotelId() {
        return hotel_id;
    }

    public void setHotelId(Long hotel_id) {
        this.hotel_id = hotel_id;
    }
}

