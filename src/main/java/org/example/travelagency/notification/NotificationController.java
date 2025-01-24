package org.example.travelagency.notification;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // Constructor to inject NotificationService
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Login success notification
    @PostMapping("/login-success")
    public String loginSuccess(@RequestParam String username) {
        // Call the correct method from NotificationService
        notificationService.printLoginSuccessMessage(username);
        return "Login successfully for user: " + username;
    }

    // Registration success notification
    @PostMapping("/register-success")
    public String registerSuccess(@RequestParam String username) {
        // Call the correct method from NotificationService
        notificationService.printRegisterSuccessMessage(username);
        return "Registration done for user: " + username;
    }

    // Booking success notification (hotel or event)
    @PostMapping("/booking-success")
    public String bookingSuccess(@RequestParam String bookingType, @RequestParam String username) {
        // Call the correct method from NotificationService
        notificationService.printBookingSuccessMessage(bookingType, username);
        return bookingType + " booking done for user: " + username;
    }

    // Event booking success notification
    @PostMapping("/event-booking-success")
    public String eventBookingSuccess(@RequestParam String username, @RequestParam String eventName) {
        // Call the correct method from NotificationService
        notificationService.printEventBookingSuccessMessage(username, eventName);
        return "Event booking done for user: " + username + " for event: " + eventName;
    }
}
