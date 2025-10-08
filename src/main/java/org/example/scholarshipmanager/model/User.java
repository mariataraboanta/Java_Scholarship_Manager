package org.example.scholarshipmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.*;

/**
 * The User class represents a user in the scholarship management system.
 * A user has a role (ADMIN or STUDENT), a unique username, a password,
 * and a unique email address.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    /**
     * Returns the unique ID of the user.
     * @return the user ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the user ID.
     * @param id the user ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the username of the user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * @param username the desired (unique) username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email address of the user.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     * @param email the unique email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the role of the user (ADMIN or STUDENT).
     * @return the user's role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * @param role the desired role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }

    /**
     * Possible roles of a user in the system.
     */
    public enum UserRole {
        ADMIN, STUDENT
    }
}
