package com.webEng.api.exception;

import java.time.LocalDateTime;

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
    ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    CsvFormatter csvFormatter = new CsvFormatter();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> catchAll(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body("EXCEPTION: " + ex.getClass());
    }

    /**
     * Main method that handles the exception.
     * 
     * @param exception The ApiException that was thrown
     * @param req       The req where we will get the accept type.
     * @return ResponseEntity with a response.
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<?> handleApiException(ApiException exception, final HttpServletRequest req) {

        // Accept header and params to determine the content type.
        // https://stackoverflow.com/questions/68554176/get-headers-when-valid-body-not-satisfy-and-exceptionhandler-hits
        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");

        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(exception.getStatus().value());
        response.setError(exception.getStatus());
        response.setMessage(exception.getMessage());
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            String csv = csvFormatter.apiExceptionToCsv(response);
            return new ResponseEntity<>(csv, exception.getStatus());
        }
        return new ResponseEntity<>(response, exception.getStatus());
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
    protected ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException exception, HttpServletRequest req) {

        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");
        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST);
        response.setMessage(exception.getMessage());
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            String csv = csvFormatter.apiExceptionToCsv(response);
            return new ResponseEntity<>(csv, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<?> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception, HttpServletRequest req) {

        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");
        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST);
        response.setMessage("Missing required parameter: " + exception.getParameterName() + " of type: "
                + exception.getParameterType());
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            String csv = csvFormatter.apiExceptionToCsv(response);
            return new ResponseEntity<>(csv, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception, HttpServletRequest req) {

        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");
        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST);
        response.setMessage("Parameter " + exception.getName() + " has invalid value: "
                + exception.getValue());
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            String csv = csvFormatter.apiExceptionToCsv(response);
            return new ResponseEntity<>(csv, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Method to handle the error message for missing params
     * 
     * @param exception The exception thrown
     * @param req       The request where we get the accept type.
     * @return The corresponding exception message.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception, HttpServletRequest req) {

        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");
        String contentType = ctn.defineContentType(accept, acceptParam);

        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST);
        response.setMessage("The object :" + exception.getObjectName() + " is badly formatted");
        response.setPath(req.getRequestURI());

        if ("text/csv".equals(contentType)) {
            String csv = csvFormatter.apiExceptionToCsv(response);
            return new ResponseEntity<>(csv, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
