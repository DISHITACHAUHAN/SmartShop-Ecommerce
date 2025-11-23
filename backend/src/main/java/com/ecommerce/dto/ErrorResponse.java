package com.ecommerce.dto;

import java.time.LocalDateTime;

/**
 * Error response DTO for API error responses
 */
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int status, String error, String message, String errorCode) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errorCode = errorCode;
    }

    // Getters and Setters
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
