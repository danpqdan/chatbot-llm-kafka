package com.sharedDTO.shared.dto;

import java.io.Serializable;

/**
 * Shared Data Transfer Object for request messages between microservices.
 */
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String message;
    private String sender;

    /**
     * Default constructor required for deserialization.
     */
    public RequestDTO() {}

    /**
     * Constructor with all parameters.
     *
     * @param message The message content
     * @param sender The sender identification
     */
    public RequestDTO(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
