package com.webEng.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webEng.api.model.Merchant;
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
    MerchantService merchantService;

    ContentTypeNegotiator ctn = new ContentTypeNegotiator();
    CsvFormatter csvFormatter = new CsvFormatter();

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
    @GetMapping("/{id}")
    public ResponseEntity<?> getMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @PathVariable Integer id, @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        Merchant tx = merchantService.getMerchant(id);
        HttpStatus status = tx == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;

        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(tx), status);
        return new ResponseEntity<>(tx, status);
    }

    /**
     * Getter for /merchants endpoint
     * 
     * retrieves all merchants
     * 
     * @param accept      Header with the accepted files
     * @param acceptParam Accept parameter for representation
     * @return ResponseEntity object with either the List of merchants or its csv
     *         representation.
     */
    @GetMapping()
    public ResponseEntity<?> getMerchants(@RequestHeader(value = "Accept", required = false) String accept,
            @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        List<Merchant> result = merchantService.getMerchants();
        HttpStatus status = result.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantListToCsv(result), status);
        return new ResponseEntity<>(result, status);
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
    @PostMapping
    public ResponseEntity<?> postMerchant(@RequestHeader(value = "Accept", required = false) String accept,
            @Valid @RequestBody Merchant merchant, @RequestParam(required = false) String acceptParam) {
        String contentType = ctn.defineContentType(accept, acceptParam);
        Merchant saved = merchantService.saveMerchant(merchant);
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
            status = HttpStatus.NOT_FOUND;
        }
        Merchant saved = merchantService.updateMerchant(merchant);

        if (contentType.equals("text/csv"))
            return new ResponseEntity<>(csvFormatter.merchantToCsv(saved), status);
        return ResponseEntity.status(HttpStatus.OK).body(status);
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
            return new ResponseEntity<>(csvFormatter.merchantToCsv(deleted), HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }

}
