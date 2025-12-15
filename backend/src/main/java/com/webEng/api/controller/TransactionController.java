package com.webEng.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webEng.api.dto.AvgAmountDto;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.model.Transaction;
import com.webEng.api.exception.ApiException;
import com.webEng.api.service.TransactionService;
import com.webEng.api.utils.CsvFormatter;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

/**
 * Controller class for the Transactions
 */
@RestController
@Validated
@RequestMapping("/transactions")
// tag
public class TransactionController {

    /**
     * The Transaction service
     */
    @Autowired
    TransactionService transactionService;

    ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    CsvFormatter csvFormatter = new CsvFormatter();

    /**
     * GET endpoint, /transaction/average
     * Header has a higher priority than parameter in Content-type defining.
     * Header>Param>Default.
     * 
     * @param accept      Header (Optional) with the content-type.
     * @param city        Name of the city.
     * @param year        Year of the transactions
     * @param month       Month of the transactions (Optional)
     * @param acceptParam Optional param that determines the content type.
     * @return ResponseEntity object with either the DTO or its csv representation.
     */

    @GetMapping(value = "/average")
    public ResponseEntity<?> getAvgAmount(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam String city, @RequestParam Integer year,
            @RequestParam(required = false) @Min(value = 1, message = "Value must be greater than 0.") @Max(value = 12, message = "month has to be less or equal than 12") Integer month,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        AvgAmountDto dto = transactionService.getAvgAmount(city, year, month);
        HttpStatus status = dto.getAvgAmount() == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        if (contentType.equals("text/csv")) {
            return new ResponseEntity<>(csvFormatter.avgAmountToCsv(dto),
                    status);
        }
        return new ResponseEntity<>(dto, status);
    }

    /**
     * GET endpoint, /transaction/total
     * 
     * Header has a higher priority than parameter in Content-type defining.
     * Header>Param>Default.
     * 
     * @param accept      Header (Optional) with the content-type.
     * @param state       Initials of the State
     * @param month       Month of the transactions (Optional)
     * @param batchSize   Size of the batch (Optional)
     * @param offset      The number of entries we want to skip (Optional)
     * @param acceptParam Optional param that determines the content type.
     * @return ResponseEntity object with either the DTO or its csv representation.
     */
    @GetMapping(value = "/total")
    public ResponseEntity<?> getTotalAmount(
            @RequestHeader(value = "Accept", required = false) String accept, @RequestParam String state,
            @RequestParam @Min(value = 1, message = "Value must be greater than 0.") @Max(value = 12, message = "month has to be less or equal than 12") Integer month,
            @RequestParam Integer batchSize, @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        List<TotalAmountDto> list = transactionService.getTotalAmounts(state, month, batchSize, offset);
        HttpStatus status = list.size() == 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.totalAmountToCsv(list), status);
        return new ResponseEntity<>(list, status);
    }

    /**
     * GET endpoint, /transaction/tb
     * 
     * Header has a higher priority than parameter in Content-type defining.
     * Header>Param>Default
     * 
     * @param accept      Header (Optional) with the content-type.
     * @param startYear   Start Year (inclusive)
     * @param endYear     End Year(Inclusive)
     * @param limit       Number of entries we want.
     * @param offset      The number of entries we want to skip (Optional)
     * @param dir         TOP or BOTTOM, to decide the ordering of the transactions
     * @param acceptParam Optional param that determines the content type.
     * @return
     */
    @GetMapping(value = "/tb")
    public ResponseEntity<?> getMaxAmount(
            @RequestHeader(value = "Accept", required = false) String accept, @RequestParam Integer startYear,
            @RequestParam Integer endYear, @RequestParam Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestParam String dir,
            @RequestParam(required = false) String acceptParam) {

        if (!dir.equalsIgnoreCase("top") && !dir.equalsIgnoreCase("bottom")) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please provide a valid ordering param [Top | Bottom]");
        }
        List<MaximumAmountDto> list = transactionService.getMaxAmount(startYear, endYear, limit, offset, dir);
        HttpStatus status = list.size() == 0 ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        String contentType = ctn.defineContentType(accept, acceptParam);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.maxAmountToCsv(list), status);
        return new ResponseEntity<>(list, status);
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "text/csv"})
    public ResponseEntity<?> getById(
            @PathVariable Integer id
    )
    {
        Transaction tx = transactionService.getById(id);

        if (tx == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(tx);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody Transaction transaction
    )
    {
        Transaction saved = transactionService.save(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @Valid @RequestBody Transaction transaction
    )
    {
        transaction.setId(id);
        Transaction updated = transactionService.save(transaction);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id
    )
    {
        transactionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<?>> findFiltered(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    )
    {
        if (month != null && (month < 1 || month > 12))
        {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid month");
        }

        List<Transaction> list = transactionService.findFiltered(clientId, year, month);
        HttpStatus status = list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        return new ResponseEntity<>(list, status);
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteFiltered(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    )
    {
        int deleteCount = transactionService.deleteFiltered(clientId, year, month);
        return ResponseEntity.ok(deleteCount);
    }


}
