package com.webEng.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webEng.api.exception.ApiException;
import com.webEng.api.model.Merchant;
import com.webEng.api.model.dto.MerchantPostDto;
import com.webEng.api.service.MerchantService;
import com.webEng.api.utils.CsvFormatter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Controller for the merchant class
 */
@RestController
@Validated
@RequestMapping("/merchants")
public class MerchantController {

    // The merchantservice
    @Autowired
    private MerchantService merchantService;

    private final ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    private final CsvFormatter csvFormatter = new CsvFormatter();

    /**
     * Getter for the /merchants/id endpoint
     * 
     * Retrieves a specific merchant
     * 
     * @param accept      Header with the accepted files
     * @param id          Id of the merchant
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the Merchant or its csv
     *         representation.
     */
    @GetMapping(value = "/{id}", produces = { "application/json", "text/csv" })
    public ResponseEntity<?> getMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id, @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        Merchant tx = merchantService.getMerchant(id);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(tx), HttpStatus.OK);
        return new ResponseEntity<>(tx, HttpStatus.OK);
    }

    /**
     * Getter for /merchants endpoint
     * 
     * retrieves all merchants
     * 
     * @param accept      Header with the accepted files
     * @param acceptParam Accept parameter for representation
     * @param limit       Number of merchants we want
     * @param offset      The index we start from
     * @return ResponseEntity object with either the List of merchants or its csv
     *         representation.
     */
    @GetMapping({ "", "/" })
    public ResponseEntity<?> getMerchants(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        List<Merchant> result = merchantService.getMerchants(limit, page);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantListToCsv(result), HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Posts a merchant on the endpoint /merchants
     * 
     * @param accept      Header with the accepted files
     * @param merchant    The merchant to save
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the Merchant or its csv
     *         representation.
     */
    @PostMapping({ "", "/" })
    public ResponseEntity<?> postMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @Valid @RequestBody MerchantPostDto merchant, @RequestParam(required = false) String acceptParam) {

        String contentType = ctn.defineContentType(accept, acceptParam);
        Merchant data = new Merchant();
        data.setMerchantCity(merchant.getMerchantCity());
        data.setMerchantState(merchant.getMerchantState());
        Merchant saved = merchantService.saveMerchant(data);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(saved), HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * 
     * @param accept      Header with the accepted files
     * @param id          Id of the merchant
     * @param acceptParam Accept parameter for representation
     * @param merchant    The merchant to update
     * @return ResponseEntity object with either the Merchant or its csv
     *         representation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> putMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id, @RequestParam(required = false) String acceptParam,
            @Valid @RequestBody Merchant merchant) {

        HttpStatus status = HttpStatus.OK;
        String contentType = ctn.defineContentType(accept, acceptParam);
        if (!merchantService.existsById(id)) {
            throw new ApiException(HttpStatus.NOT_FOUND, "The resource with id: " + id + " was not found");
        }
        merchant.setId(id);
        Merchant saved = merchantService.updateMerchant(merchant);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(saved), status);
        return new ResponseEntity<>(saved, status);
    }

    /**
     * 
     * @param accept      Header with the accepted files
     * @param id          Id of the merchant
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the Merchant or its csv
     *         representation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id, @RequestParam(required = false) String acceptParam) {
        Merchant deleted = merchantService.getMerchant(id);
        String contentType = ctn.defineContentType(accept, acceptParam);
        merchantService.deleteMerchant(id);
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(deleted), HttpStatus.NO_CONTENT);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(deleted);
    }

}
