package org.example.travelagency.userManagement;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register the user using DTO
    public User registerUser(UserRegistrationDTO registrationDTO) {
        // Create new user and set details
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPhone(registrationDTO.getPhone());

        // Encrypt the password before saving it to the database
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        // Save the user in the repository
        return userRepository.save(user);
    }

    public Optional<User> authenticateUser(String email, String password) {
        // Find the user by email
        Optional<User> user = userRepository.findByEmail(email);
        // Verify the password with BCrypt's matching method
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty(); // Invalid credentials
    }

    public boolean resetPassword(String email, String newPassword, String token) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getResetPasswordToken().equals(token)) {
            User updatedUser = user.get();
            // Encrypt the new password before saving
            updatedUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(updatedUser);
            return true;
        }
        return false;
    }

    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(User user) {
        return userRepository.save(user);
    }
}
