package com.webEng.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.exception.ApiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.webEng.api.model.Transaction;
import com.webEng.api.repository.RepoTransaction;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;

/**
 * @author Miguel Akira López Asano
 * 
 *         Class that implements the TransactionService interface.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    RepoTransaction repoTransaction;

    /**
     * Invoques the getAvgAmount method from the repository.
     * 
     * @param city  Name of the city
     * @param year  Year of the transaction
     * @param month Month of the transaction (Null if we are searching for a year)
     * @return avg amount
     */
    @Override
    public Double getAvgAmount(String city, Integer year, Integer month) {
        if (city == null || year == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "City and Year are required parameters");
        try {
            Double res = repoTransaction.getAvgAmount(city, year, month);
            if (res == null)
                throw new ApiException(HttpStatus.NOT_FOUND, "There was no data found");
            return res;
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing the database.");
        }
    }

    /**
     * Invoques the getTotalAmount method from the repository.
     * 
     * @param state     Name of the state
     * @param month     Month of the transaction
     * @param batchSize Desired batch size.
     * @param offset    How many elements we skip before returning element.
     * @return object with the TotalAmountDto
     */
    public List<TotalAmountDto> getTotalAmounts(String state, Integer month, Integer batchSize,
            Integer offset) {
        if (state == null || month == null || batchSize == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "State, month and batch size are required parameters");
        try {
            return repoTransaction.getTotalAmount(state, month, batchSize, offset);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing the database.");
        }
    }

    /**
     * Invoques the corresponding getMaximumAmount method depending on dir from the
     * repository.
     * 
     * @param startYear Left bound for ythe Year we want to search
     * @param endYear   Right bound for the year we want to search
     * @param limit     Limit of number in answers
     * @param offset    The offset for retrieving answrs
     * @param dir       The direction that we want to retrieve.
     * @return object with the MaximumAmountDto
     */
    public List<MaximumAmountDto> getMaxAmount(Integer startYear, Integer endYear, Integer limit,
            Integer offset, String dir) {
        // Might be good to change this to an enum.
        if (startYear == null || endYear == null || limit == null || dir == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "startYear, endYear, limit and dir are required parameters");
        try {
            if (dir.equalsIgnoreCase("ASC"))
                return repoTransaction.getMaximumAmountBottom(startYear, endYear, limit, offset);
            else if (dir.equalsIgnoreCase("DESC"))
                return repoTransaction.getMaximumAmountTop(startYear, endYear, limit, offset);
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid dir parameter");
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing the database.");
        }

    }
}
