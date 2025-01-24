package org.example.travelagency.userManagement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Add the method to find a user by email and password
   // Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}

