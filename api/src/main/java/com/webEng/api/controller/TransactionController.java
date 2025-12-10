package com.webEng.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webEng.api.dto.AvgAmountDto;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.exception.ApiException;
import com.webEng.api.service.TransactionService;
import com.webEng.api.utils.CsvFormatter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
//import javax.validation.constraints.*;

import java.util.List;

/**
 * Controller class for the Transactions
 */
@RestController
@RequestMapping("/transactions")
// tag
public class TransactionController {

    @Autowired
    TransactionService service;

    ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    CsvFormatter csvFormatter = new CsvFormatter();

    // For now, header>param in priority for data type.
    @GetMapping(value = "/average")
    // TODO: add not found or no content.
    public ResponseEntity<?> getAvgAmount(@RequestHeader(value = "Accept", required = false) String accepts,
            @RequestParam String city, @RequestParam Integer year,
            @RequestParam(required = false) @Min(value = 1, message = "Value must be greater than 0.") @Max(value = 12, message = "month has to be less or equal than 12") Integer month,
            @RequestParam(required = false, defaultValue = "application/json") String accept) {

        String contentType = ctn.defineContentType(accepts, accept);
        AvgAmountDto dto = service.getAvgAmount(city, year, month);
        HttpStatus status = dto.getAvgAmount() == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        if (contentType.equals("text/csv")) {
            return new ResponseEntity<>(csvFormatter.avgAmountToCsv(dto),
                    status);
        }
        return new ResponseEntity<>(dto, status);
    }

    @GetMapping(value = "/total", produces = { "application/json", "text/csv" })
    public ResponseEntity<?> getTotalAmount(
            @RequestHeader(value = "Accept", required = false) String accepts, @RequestParam String state,
            @RequestParam @Min(value = 1, message = "Value must be greater than 0.") @Max(value = 12, message = "month has to be less or equal than 12") Integer month,
            @RequestParam Integer batchSize, @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false, defaultValue = "application/json") String accept) {

        String contentType = ctn.defineContentType(accepts, accept);
        if (month < 1 || month > 12)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Month has to be between 1 and 12");
        List<TotalAmountDto> list = service.getTotalAmounts(state, month, batchSize, offset);
        HttpStatus status = list.size() == 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.totalAmountToCsv(list), status);
        return new ResponseEntity<>(list, status);
    }

    @GetMapping(value = "/tb", produces = { "application/json", "text/csv" })
    public ResponseEntity<List<MaximumAmountDto>> getMaxAmount(
            @RequestHeader(value = "Accept", required = false) String accepts, @RequestParam Integer startYear,
            @RequestParam Integer endYear, @RequestParam Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam String dir,
            @RequestParam(required = false, defaultValue = "application/json") String accept) {
        // TODO Change to the top or bottom sorting order.
        if (!dir.equalsIgnoreCase("ASC") && !dir.equalsIgnoreCase("DESC")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please provide a valid ordering param [ASC | DESC]");
        }
        return new ResponseEntity<>(service.getMaxAmount(startYear, endYear, limit, offset, dir), HttpStatus.OK);
    }

}
