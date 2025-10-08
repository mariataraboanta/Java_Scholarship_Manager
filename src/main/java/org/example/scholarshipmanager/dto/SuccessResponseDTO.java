package org.example.scholarshipmanager.dto;

/**
 * Data Transfer Object representing a successful operation response.
 * Contains a success message and optional data.
 */
public class SuccessResponseDTO {
    private String message;
    private Object data;

    /**
     * Constructs a new SuccessResponse with a message and data.
     *
     * @param message Success message
     * @param data Optional data associated with the response
     */
    public SuccessResponseDTO(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    /**
     * Gets the success message.
     *
     * @return Success message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the success message.
     *
     * @param message Success message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the data associated with the response.
     *
     * @return Response data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data associated with the response.
     *
     * @param data Response data
     */
    public void setData(Object data) {
        this.data = data;
    }
}