package org.example.scholarshipmanager.service;

import org.example.scholarshipmanager.model.User;
import org.example.scholarshipmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing User entities.
 * Provides methods for CRUD operations and queries related to users.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserService with the given UserRepository.
     *
     * @param userRepository the repository used to perform database operations on User entities.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their unique ID.
     *
     * @param id the ID of the user to find.
     * @return an Optional containing the user if found, or empty if not found.
     */
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user.
     * @return an Optional containing the user if found, or empty if not found.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email of the user.
     * @return an Optional containing the user if found, or empty if not found.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Checks if a user exists with the specified username.
     *
     * @param username the username to check.
     * @return true if a user with the given username exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Checks if a user exists with the specified email.
     *
     * @param email the email to check.
     * @return true if a user with the given email exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Saves a user entity to the database.
     * If the user already exists, it updates the existing user.
     *
     * @param user the user entity to save.
     * @return the saved user entity.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     */
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }
}
