package com.webEng.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webEng.api.dto.ClientSummaryDto;
import com.webEng.api.dto.MerchantSummaryDto;
import com.webEng.api.dto.StateSummaryDto;
import com.webEng.api.dto.YearSummaryDto;
import com.webEng.api.service.SummaryService;
import com.webEng.api.utils.CsvFormatter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/summaries")
@Validated
/**
 * Controller for the data summary endpoint.
 */
public class SummaryController {

    @Autowired
    SummaryService summaryService;

    ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    CsvFormatter csvFormatter = new CsvFormatter();

    /**
     * Getter for the /clients endpoint that returns a summary of the clients
     * transactions.
     * 
     * @param accept      Header with the accepted files
     * @param year        Year of the summary
     * @param month       month of the summary
     * @param view        avg, total, count
     * @param limit       Number of results we want
     * @param offset      The index we start from
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the List of ClientSummary or its
     *         csv
     *         representation.
     */
    @GetMapping("/clients")
    public ResponseEntity<?> getClientSummary(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false, defaultValue = "total") String view,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        List<ClientSummaryDto> result = summaryService.getClientSummary(year, month, view, limit, offset);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.clientSummaryToCsv(result), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Getter for the /merchants endpoint that returns a summary of the merchants
     * transactions.
     * 
     * @param accept      Header with the accepted files
     * @param year        Year of the summary
     * @param month       month of the summary
     * @param state       state of the merchant
     * @param city        city of the merchant
     * @param limit       Number of results we want
     * @param offset      The index we start from
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the List of MerchantSummary or its
     *         csv
     *         representation.
     */
    @GetMapping("/merchants")
    public ResponseEntity<?> getMerchantsSummary(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String city,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        List<MerchantSummaryDto> result = summaryService.getMerchantSummary(year, month, state, city, limit, offset);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantSummaryToCsv(result), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Getter for the /state endpoint that returns a summary of the states
     * transactions.
     * 
     * @param accept      Header with the accepted files
     * @param year        Year of the summary
     * @param month       month of the summary
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the List of StateSummary or its
     *         csv
     *         representation.
     */
    @GetMapping("/state")
    public ResponseEntity<?> getStateSummary(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        List<StateSummaryDto> result = summaryService.getStateSummary(year, month);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.stateSummaryToCsv(result), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Getter for the /year endpoint that returns a summary of the merchants
     * transactions.
     * 
     * @param accept      Header with the accepted files
     * @param start       Start Year of the summary
     * @param end         End year of the summary If start == end then same year.
     * @param detail      quarter , month
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the List of Year summary or its
     *         csv
     *         representation.
     */
    @GetMapping("/year")
    public ResponseEntity<?> getYearSummary(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam Integer start,
            @RequestParam Integer end,
            @RequestParam(required = false, defaultValue = "month") String detail,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        List<YearSummaryDto> result = summaryService.getYearSummary(start, end, detail);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.yearSummaryToCsv(result), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
