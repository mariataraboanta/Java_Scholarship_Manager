package org.example.scholarshipmanager.dto;

import org.example.scholarshipmanager.model.Department;

import java.math.BigDecimal;

/**
 * Data Transfer Object for user registration requests.
 * Contains all information needed to register a new user and create a student profile.
 */
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String enrollmentNumber;
    private Integer departmentId;
    private BigDecimal financialNeedScore;
    private Integer communityServiceHours;
    private BigDecimal leadershipScore;
    private Integer yearOfStudy;
    private BigDecimal gpa;
    /**
     * Default constructor.
     */
    public RegisterRequestDTO() {
    }

    /**
     * Constructs a new RegisterRequestDTO with all required fields.
     *
     * @param username The desired username
     * @param email The user's email address
     * @param password The desired password
     * @param firstName The student's first name
     * @param lastName The student's last name
     * @param enrollmentNumber The student's enrollment number
     */
    public RegisterRequestDTO(String username, String email, String password,
                              String firstName, String lastName, String enrollmentNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enrollmentNumber = enrollmentNumber;
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
     * Gets the email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address.
     *
     * @param email The email address to set
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * Gets the first name.
     *
     * @return The first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     *
     * @param firstName The first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     *
     * @return The last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     *
     * @param lastName The last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the enrollment number.
     *
     * @return The enrollment number
     */
    public String getEnrollmentNumber() {
        return enrollmentNumber;
    }

    /**
     * Sets the enrollment number.
     *
     * @param enrollmentNumber The enrollment number to set
     */
    public void setEnrollmentNumber(String enrollmentNumber) {
        this.enrollmentNumber = enrollmentNumber;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }
    public BigDecimal getFinancialNeedScore() {
        return financialNeedScore;
    }
    public void setFinancialNeedScore(BigDecimal financialNeedScore) {
        this.financialNeedScore = financialNeedScore;
    }
    public BigDecimal getLeadershipScore() {
        return leadershipScore;
    }
    public void setLeadershipScore(BigDecimal leadershipScore) {
        this.leadershipScore = leadershipScore;
    }
    public Integer getCommunityServiceHours(){
        return communityServiceHours;
    }
    public Integer getYearOfStudy() {
        return yearOfStudy;
    }
    public void setYearOfStudy(Integer yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
    public BigDecimal getGpa() {
        return gpa;
    }
}