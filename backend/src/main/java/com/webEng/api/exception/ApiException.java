package com.webEng.api.exception;

import org.springframework.http.HttpStatus;

/**
 * @author López Asano Miguel Akira
 *         Class for the ApiException(bad request, not found and internal server
 *         error)
 */
public class ApiException extends RuntimeException {

    private HttpStatus status;

    /**
     * Constructs a new ApiException with the specified HTTP status and detail
     * message.
     * 
     * @param status  the HTTP status code
     * @param message detail message
     */
    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    /**
     * Returns the HTTP status code associated with this exception.
     * 
     * @return the HTTP status code
     */
    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code for this exception.
     * 
     * @param status the new HTTP status code
     */
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
