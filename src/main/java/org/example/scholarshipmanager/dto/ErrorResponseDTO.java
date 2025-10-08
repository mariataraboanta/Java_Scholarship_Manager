package org.example.scholarshipmanager.dto;

/**
 * Data Transfer Object representing an error response.
 * Contains an error type and a detailed error message.
 */
public class ErrorResponseDTO {
    private String error;
    private String message;

    /**
     * Constructs a new ErrorResponse with an error type and message.
     *
     * @param error Error type or title
     * @param message Detailed error message
     */
    public ErrorResponseDTO(String error, String message) {
        this.error = error;
        this.message = message;
    }

    /**
     * Gets the error type or title.
     *
     * @return Error type
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error type or title.
     *
     * @param error Error type
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Gets the detailed error message.
     *
     * @return Error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the detailed error message.
     *
     * @param message Error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}