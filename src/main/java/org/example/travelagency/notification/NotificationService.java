package org.example.travelagency.notification;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // Function to print login success message
    public String printLoginSuccessMessage(String username) {
        String message = "Login successful for user: " + username;
        System.out.println(message);
        return message;
    }

    // Function to print registration success message
    public String printRegisterSuccessMessage(String username) {
        String message = "Registration successful for user: " + username;
        System.out.println(message);
        return message;
    }

    // Function to print booking success message (for hotel or event)
    public String printBookingSuccessMessage(String bookingType, String username) {
        String message = bookingType + " booking done for user: " + username;
        System.out.println(message);
        return message;
    }

    // Function to print event booking success message
    public String printEventBookingSuccessMessage(String username, String eventName) {
        String message = "Event booking done for user: " + username + " for event: " + eventName;
        System.out.println(message);
        return message;
    }
}
