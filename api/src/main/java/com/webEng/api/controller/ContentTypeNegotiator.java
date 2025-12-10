package com.webEng.api.controller;

import org.springframework.http.HttpStatus;

import com.webEng.api.exception.ApiException;

/**
 * Class that decides what type of media the server is returning.
 */
public class ContentTypeNegotiator {

    /**
     * @author Miguel Akira López Asano
     *         Defined the content type for the request.
     *         If both header and param are present, the header has higher priority
     *         and
     *         overwrites the param.
     *         Default value is json.
     * 
     * @param acceptHeader     The accept header with the content type. Null if
     *                         there's no accept header.
     * @param contentTypeParam The contentType param with the content type. Null if
     *                         there's no param.
     * @return The content type or throws HTTP error code 406.
     */
    public String defineContentType(String acceptHeader, String contentTypeParam) {
        String contentType = "application/json";
        if (acceptHeader != null && acceptHeader.contains("text/csv")) {
            contentType = "text/csv";
        } else if (contentTypeParam != null)
            contentType = contentTypeParam;
        if (!contentType.equals("application/json") && !contentType.equals("text/csv")) {
            throw new ApiException(HttpStatus.NOT_ACCEPTABLE, "Media type not supported");
        }
        return contentType;
    }
}
