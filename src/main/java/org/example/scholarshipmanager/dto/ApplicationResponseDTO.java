package org.example.scholarshipmanager.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for application responses.
 * Contains comprehensive information about scholarship applications, including
 * application details, student information, and scholarship information.
 * Used for API responses when retrieving application data.
 *
 * @author Generated
 * @version 1.0
 * @since 1.0
 */
public class ApplicationResponseDTO {

    /**
     * Unique identifier for the application.
     */
    private Integer id;

    /**
     * Date and time when the application was submitted.
     */
    private LocalDateTime applicationDate;

    /**
     * Current status of the application (e.g., PENDING, APPROVED, REJECTED).
     */
    private String status;

    /**
     * Match score between the student and scholarship, indicating compatibility.
     */
    private BigDecimal matchScore;

    /**
     * Additional notes or comments about the application.
     */
    private String notes;

    /**
     * Date and time when the application was reviewed.
     */
    private LocalDateTime reviewedAt;

    /**
     * Identifier of the user who reviewed the application.
     */
    private Integer reviewerId;

    /**
     * Unique identifier of the student who submitted the application.
     */
    private Integer studentId;

    /**
     * Full name of the student.
     */
    private String studentName;

    /**
     * Student's enrollment number or student ID.
     */
    private String studentEnrollmentNumber;

    /**
     * Student's email address.
     */
    private String studentEmail;

    /**
     * Unique identifier of the scholarship being applied for.
     */
    private Integer scholarshipId;

    /**
     * Name of the scholarship.
     */
    private String scholarshipName;

    /**
     * Monetary amount of the scholarship.
     */
    private BigDecimal scholarshipAmount;

    /**
     * Default constructor for ApplicationResponseDTO.
     * Creates an empty instance that can be populated using setter methods.
     */
    public ApplicationResponseDTO() {}

    /**
     * Gets the unique identifier for the application.
     *
     * @return the application ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the application.
     *
     * @param id the application ID to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the date and time when the application was submitted.
     *
     * @return the application submission date
     */
    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    /**
     * Sets the date and time when the application was submitted.
     *
     * @param applicationDate the application submission date to set
     */
    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    /**
     * Gets the current status of the application.
     *
     * @return the application status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the application.
     *
     * @param status the application status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the match score between the student and scholarship.
     *
     * @return the match score, or null if not calculated
     */
    public BigDecimal getMatchScore() {
        return matchScore;
    }

    /**
     * Sets the match score between the student and scholarship.
     *
     * @param matchScore the match score to set
     */
    public void setMatchScore(BigDecimal matchScore) {
        this.matchScore = matchScore;
    }

    /**
     * Gets additional notes or comments about the application.
     *
     * @return the application notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets additional notes or comments about the application.
     *
     * @param notes the application notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the date and time when the application was reviewed.
     *
     * @return the review date, or null if not yet reviewed
     */
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    /**
     * Sets the date and time when the application was reviewed.
     *
     * @param reviewedAt the review date to set
     */
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    /**
     * Gets the identifier of the user who reviewed the application.
     *
     * @return the reviewer ID, or null if not yet reviewed
     */
    public Integer getReviewerId() {
        return reviewerId;
    }

    /**
     * Sets the identifier of the user who reviewed the application.
     *
     * @param reviewerId the reviewer ID to set
     */
    public void setReviewerId(Integer reviewerId) {
        this.reviewerId = reviewerId;
    }

    /**
     * Gets the unique identifier of the student who submitted the application.
     *
     * @return the student ID
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * Sets the unique identifier of the student who submitted the application.
     *
     * @param studentId the student ID to set
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the full name of the student.
     *
     * @return the student's full name
     */
    public String getStudentName() {
        return studentName;
    }

    /**
     * Sets the full name of the student.
     *
     * @param studentName the student's full name to set
     */
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /**
     * Gets the student's enrollment number or student ID.
     *
     * @return the student's enrollment number
     */
    public String getStudentEnrollmentNumber() {
        return studentEnrollmentNumber;
    }

    /**
     * Sets the student's enrollment number or student ID.
     *
     * @param studentEnrollmentNumber the student's enrollment number to set
     */
    public void setStudentEnrollmentNumber(String studentEnrollmentNumber) {
        this.studentEnrollmentNumber = studentEnrollmentNumber;
    }

    /**
     * Gets the student's email address.
     *
     * @return the student's email address
     */
    public String getStudentEmail() {
        return studentEmail;
    }

    /**
     * Sets the student's email address.
     *
     * @param studentEmail the student's email address to set
     */
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    /**
     * Gets the unique identifier of the scholarship being applied for.
     *
     * @return the scholarship ID
     */
    public Integer getScholarshipId() {
        return scholarshipId;
    }

    /**
     * Sets the unique identifier of the scholarship being applied for.
     *
     * @param scholarshipId the scholarship ID to set
     */
    public void setScholarshipId(Integer scholarshipId) {
        this.scholarshipId = scholarshipId;
    }

    /**
     * Gets the name of the scholarship.
     *
     * @return the scholarship name
     */
    public String getScholarshipName() {
        return scholarshipName;
    }

    /**
     * Sets the name of the scholarship.
     *
     * @param scholarshipName the scholarship name to set
     */
    public void setScholarshipName(String scholarshipName) {
        this.scholarshipName = scholarshipName;
    }

    /**
     * Gets the monetary amount of the scholarship.
     *
     * @return the scholarship amount
     */
    public BigDecimal getScholarshipAmount() {
        return scholarshipAmount;
    }

    /**
     * Sets the monetary amount of the scholarship.
     *
     * @param scholarshipAmount the scholarship amount to set
     */
    public void setScholarshipAmount(BigDecimal scholarshipAmount) {
        this.scholarshipAmount = scholarshipAmount;
    }
}