package org.example.scholarshipmanager.dto;

/**
 * Data Transfer Object for user login requests.
 * Contains credentials needed for authentication.
 */
public class LoginRequestDTO {
    private String username;
    private String password;

    /**
     * Default constructor.
     */
    public LoginRequestDTO() {
    }

    /**
     * Constructs a new LoginRequestDTO with specified username and password.
     *
     * @param username The user's username
     * @param password The user's password
     */
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}