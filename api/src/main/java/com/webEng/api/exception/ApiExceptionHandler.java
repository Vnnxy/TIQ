package com.webEng.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.webEng.api.controller.ContentTypeNegotiator;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author López Asano Miguel Akira
 *         Handler for the API related exceptions
 */
@ControllerAdvice
public class ApiExceptionHandler {
    ContentTypeNegotiator ctn = new ContentTypeNegotiator();

    /**
     * Main method that handles the exception.
     * 
     * @param exception The ApiException that was thrown
     * @param request   The request for the URI
     * @return ResponseEntity with a response.
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<?> handleApiException(ApiException exception, HttpServletRequest req) {

        // Read directly from the request (Spring will ALWAYS provide these)
        String accept = req.getHeader("Accept");
        String acceptParam = req.getParameter("acceptParam");

        // Your existing content type resolver
        String contentType = ctn.defineContentType(accept, acceptParam);

        // Build JSON response
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(exception.getStatus().value());
        response.setError(exception.getStatus());
        response.setMessage(exception.getMessage());
        response.setPath(req.getRequestURI());

        // ---- CSV RESPONSE ----
        if ("text/csv".equals(contentType)) {
            String csv = String.format(
                    "timestamp,status,error,message,path\n\"%s\",%d,%s,\"%s\",\"%s\"",
                    response.getTimestamp(),
                    response.getStatus(),
                    response.getError(),
                    response.getMessage(),
                    response.getPath());

            return ResponseEntity
                    .status(exception.getStatus())
                    .header("Content-Type", "text/csv")
                    .body(csv);
        }

        // ---- JSON RESPONSE (default) ----
        return new ResponseEntity<>(response, exception.getStatus());
    }

}
