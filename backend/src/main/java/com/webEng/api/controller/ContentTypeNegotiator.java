package com.webEng.api.controller;

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
     * @return The content type.
     */
    public String defineContentType(String acceptHeader, String contentTypeParam) {
        final String JSON = "application/json";
        final String CSV = "text/csv";
        if (acceptHeader != null && !acceptHeader.equals("*/*")) {
            if (acceptHeader.contains("text/csv"))
                return CSV;

            return JSON;
        }
        if (contentTypeParam != null) {
            if (contentTypeParam.equalsIgnoreCase("text/csv"))
                return CSV;
            return JSON;
        }
        return JSON;
    }
}
