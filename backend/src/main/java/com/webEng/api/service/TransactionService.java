package com.webEng.api.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.webEng.api.model.Transaction;
import com.webEng.api.dto.*;

/**
 * @author Miguel Akira López Asano
 * 
 *         Interface for the service layer of the transactions.
 *         The purpose of this interface is to mantain SOLID principles.
 */
public interface TransactionService {

    TransactionDto getById(Integer id);

    TransactionDto save(TransactionDto dto); // also update

    void deleteById(Integer id);

    List<TransactionDto> findFiltered(Integer clientId, Integer year, Integer month, Integer limit);

    List<TransactionDto> deleteFiltered(Integer clientId, Integer year, Integer month, Integer limit);

    /**
     * Invoques the getAvgAmount method from the repository.
     * 
     * @param city  Name of the city
     * @param year  Year of the transaction
     * @param month Month of the transaction (Null if we are searching for a year)
     * @return avg amount.
     */
    public AvgAmountDto getAvgAmount(String city, Integer year, Integer month);

    /**
     * Invoques the getTotalAmount method from the repository.
     * 
     * @param state     Name of the state
     * @param month     Month of the transaction
     * @param batchSize Desired batch size.
     * @param offset    How many elements we skip before returning element.
     * @return List containing TotalAmountDto
     */
    public List<TotalAmountDto> getTotalAmounts(String state, Integer month, Integer batchSize,
            Integer offset);

    /**
     * Invoques the corresponding getMaximumAmount method depending on dir from the
     * repository.
     * 
     * @param startYear Left bound for ythe Year we want to search
     * @param endYear   Right bound for the year we want to search
     * @param limit     Limit of number in answers
     * @param offset    The offset for retrieving answrs
     * @param dir       The direction that we want to retrieve.
     * @return List containing MaximumAmountDto
     */
    public List<MaximumAmountDto> getMaxAmount(Integer startYear, Integer endYear, Integer limit,
            Integer offset, String dir);

    boolean existsById(Integer id);

}
