package com.webEng.api.exception;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.webEng.api.controller.ContentTypeNegotiator;
import com.webEng.api.utils.CsvFormatter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

/**
 * 
 * @author López Asano Miguel Akira
 *         Handler for the API related exceptions
 */
@ControllerAdvice
public class ApiExceptionHandler {

    private final ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    private final CsvFormatter csvFormatter = new CsvFormatter();

    /**
     * Handler for all other exceptions.
     * 
     * @param ex exception
     * @return 500 error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> catchAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body("EXCEPTION: " + ex.getClass());
    }

    /**
     * Helper method that builds a response and set the required parameters
     * 
     * @param status  The HTTP Status code
     * @param message The message
     * @param req     The request
     * @return ResponseEntity in the desired representation
     */
    private ResponseEntity<?> buildResponse(HttpStatus status, String message, HttpServletRequest req) {
        // Accept header and params to determine the content type.
        // https://stackoverflow.com/questions/68554176/get-headers-when-valid-body-not-satisfy-and-exceptionhandler-hits
        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");
        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(status.value());
        response.setError(status);
        response.setMessage(message);
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            return new ResponseEntity<>(csvFormatter.apiExceptionToCsv(response), status);
        }
        return new ResponseEntity<>(response, status);
    }

    /**
     * Main method that handles the exception.
     * 
     * @param exception The ApiException that was thrown
     * @param req       The req where we will get the accept type.
     * @return ResponseEntity with a response.
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException exception, final HttpServletRequest req) {

        return buildResponse(exception.getStatus(), exception.getMessage(), req);
    }

    /**
     * Method to handle the error message for validation by @min and @max
     * annotations.
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest req) {

        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), req);
    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception, HttpServletRequest req) {
        String message = "Missing required parameter: " + exception.getParameterName() + " of type: "
                + exception.getParameterType();

        return buildResponse(HttpStatus.BAD_REQUEST, message, req);
    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception, HttpServletRequest req) {

        String message = "Parameter " + exception.getName() + " has invalid value: " + exception.getValue();
        return buildResponse(HttpStatus.BAD_REQUEST, message, req);

    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, HttpServletRequest req) {

        String message = "Validation failed for object: " + exception.getObjectName();
        return buildResponse(HttpStatus.BAD_REQUEST, message, req);
    }

    /**
     * Method to handle Deletion of merchant when its referenced
     * 
     * @param ex  exception thrown
     * @param req The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleFKViolation(DataIntegrityViolationException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.CONFLICT, "Cannot delete merchant: existing transactions reference it", req);
    }

}
