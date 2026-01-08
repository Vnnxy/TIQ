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
import com.webEng.api.dto.TransactionDto;
import com.webEng.api.dto.TransactionWriteDto;
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
     * @return ResponseEntity object with either the DTO or its csv representation.
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

    /**
     * GET endpoint for /transactions/{id}
     * 
     * Retrieves a specific transaction.
     * 
     * @param id Id of the transaction we want to retrieve.
     * @return ResponseEntity object with either the DTO or its csv representation.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id, @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        TransactionDto tx = transactionService.getById(id);
        HttpStatus status = tx == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.transactionToCsv(tx), status);// to csv
        return new ResponseEntity<>(tx, status);
    }

    /**
     * Creates a new transaction in the database
     *
     * @param accept Header (Optional) with content type
     * @param transaction Transaction (post dto) to create
     * @param acceptParam Optional content type param
     * @return the created transaction
     */
    @PostMapping
    public ResponseEntity<?> create(
            @RequestHeader(value = "Accept", required = false) String accept,
            @Valid @RequestBody TransactionWriteDto transaction,
            @RequestParam(required = false) String acceptParam) {

        TransactionDto dto = new TransactionDto(transaction);
        TransactionDto saved = transactionService.save(dto);

        String contentType = ctn.defineContentType(accept, acceptParam);
        if (contentType.equals("text/csv"))
            return ResponseEntity.status(HttpStatus.CREATED).body(csvFormatter.transactionToCsv(saved));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Updates an existing transaction
     *
     * @param id id of transaction to update
     * @param transaction
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id,
            @Valid @RequestBody TransactionWriteDto transaction,
            @RequestParam(required = false) String acceptParam) {
        if (!transactionService.existsById(id))
            return ResponseEntity.notFound().build();

        TransactionDto saveDto = new TransactionDto(id, transaction);
        TransactionDto updated = transactionService.save(saveDto);

        String contentType = ctn.defineContentType(accept, acceptParam);
        if (contentType.equals("text/csv"))
            return ResponseEntity.ok(csvFormatter.transactionToCsv(updated));
        return ResponseEntity.ok(updated);
    }

    /**
     * deletes by id 🤷‍♀️
     * @param id
     * @return deleted transaction
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id,
            @RequestParam(required = false) String acceptParam) {
        var deleted = transactionService.getById(id);
        transactionService.deleteById(id);

        if (ctn.defineContentType(accept, acceptParam).equals("text/csv"))
            return ResponseEntity.ok(csvFormatter.transactionToCsv(deleted));
        return ResponseEntity.ok(deleted);
    }

    /**
     * Finds transactions that match the filter
     *
     * @param accept content type header
     * @param clientId
     * @param year
     * @param month
     * @param limit limit to how many items are returned
     * @param acceptParam content type parameter
     * @return list of matching transactions
     */
    @GetMapping
    public ResponseEntity<?> findFiltered(
            @RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = true) Integer clientId, // required should be updated in endpoint doc too
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) Integer month,
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false) String acceptParam) {
        if (month != null && (month < 1 || month > 12)) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid month");
        }

        var list = transactionService.findFiltered(clientId, year, month, limit);
        HttpStatus status = list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        if (ctn.defineContentType(accept, acceptParam).equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.transactionToCsv(list), status);
        return new ResponseEntity<>(list, status);
    }

    /**
     * Deletes transactions that match the filter
     *
     * @param accept content type header
     * @param clientId
     * @param year
     * @param month
     * @param limit limit to how many items are deleted
     * @param acceptParam content type parameter
     * @return list of deleted transactions
     */
    @DeleteMapping
    public ResponseEntity<?> deleteFiltered(
            @RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = true) Integer clientId,
            @RequestParam(required = true) Integer year,
            @RequestParam(required = true) Integer month,
            @RequestParam(required = false, defaultValue = "100") Integer limit,
            @RequestParam(required = false) String acceptParam) {
        var list = transactionService.deleteFiltered(clientId, year, month, limit);
        HttpStatus status = list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        if (ctn.defineContentType(accept, acceptParam).equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.transactionToCsv(list), status);
        return new ResponseEntity<>(list, status);
    }

}
