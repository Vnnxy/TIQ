package com.webEng.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
 * @author López Asano Miguel Akira
 *         Handler for the API related exceptions
 */
@ControllerAdvice
public class ApiExceptionHandler {
    /**
     * Main method that handles the exception.
     * 
     * @param exception The ApiException that was thrown
     * @param request   The request for the URI
     * @return ResponseEntity with a response.
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<?> handleApiException(ApiException exception, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(exception.getStatus().value());
        response.setError(exception.getStatus());
        response.setMessage(exception.getMessage());
        response.setPath(((ServletWebRequest) request).getRequest().getRequestURI().toString());

        return new ResponseEntity<>(response, response.getError());
    }

}
