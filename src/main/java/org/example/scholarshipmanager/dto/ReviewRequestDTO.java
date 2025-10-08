package org.example.scholarshipmanager.dto;

/**
 * DTO representing a review request, typically used by reviewers to provide notes or feedback.
 */
public class ReviewRequestDTO {

    /**
     * Optional notes or comments provided during the review.
     */
    private String notes;

    /**
     * Default constructor.
     */
    public ReviewRequestDTO() {}

    /**
     * Constructs a review request with the given notes.
     *
     * @param notes the review notes or comments
     */
    public ReviewRequestDTO(String notes) {
        this.notes = notes;
    }

    /**
     * Returns the review notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the review notes.
     *
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
