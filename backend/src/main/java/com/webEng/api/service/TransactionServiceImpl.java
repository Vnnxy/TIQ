package com.webEng.api.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webEng.api.dto.AvgAmountDto;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.exception.ApiException;

import org.springframework.beans.factory.annotation.Autowired;

import com.webEng.api.model.Transaction;
import com.webEng.api.model.Merchant;
import com.webEng.api.repository.RepoTransaction;
import com.webEng.api.repository.RepoMerchant;

/**
 * @author Miguel Akira López Asano
 * 
 *         Class that implements the TransactionService interface.
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    RepoTransaction repoTransaction;

    @Autowired
    RepoMerchant repoMerchant;

    /**
     * Invoques the getAvgAmount method from the repository.
     * 
     * @param city  Name of the city
     * @param year  Year of the transaction
     * @param month Month of the transaction (Null if we are searching for a year)
     * @return avg amount
     */
    @Override
    public AvgAmountDto getAvgAmount(String city, Integer year, Integer month) {
        if (city == null || year == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "City and Year are required parameters");
        try {
            Double res = repoTransaction.getAvgAmount(city, year, month);
            return new AvgAmountDto(res);
        } catch (Exception e) {
            e.getStackTrace();
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
            if (dir.equalsIgnoreCase("bottom"))
                return repoTransaction.getMaximumAmountBottom(startYear, endYear, limit, offset);
            else if (dir.equalsIgnoreCase("top"))
                return repoTransaction.getMaximumAmountTop(startYear, endYear, limit, offset);
            // This case should never happen as there is validation in the controller.
            else
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid dir parameter");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Error accessing the database.");
        }

    }

    /** Retrieve transaction by ID */
    @Override // the deprecated JpaRepository function
    public Transaction getById(Integer id)
    {
        if (id == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "id is a required field");
        return repoTransaction.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transaction not found with id = " + id));
    }

    /** Create or update a transaction */
    @Override
    public Transaction save(Transaction transaction)
    {
        if (transaction == null || transaction.getClientId() == null
                || transaction.getAmount() == null || transaction.getDate() == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Missing data");

        try
        {
            // fetch merchant entity id
            Integer merchantId = transaction.getMerchant().getId();
            Merchant merchant = repoMerchant.findById(merchantId)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Merchant not found"));

            transaction.setMerchant(merchant);
            return repoTransaction.save(transaction);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error while saving transaction");
        }
    }

    /** Delete transaction by ID */
    @Override
    public void deleteById(Integer id)
    {
        if (id == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "id is a required parameter");
        if (!repoTransaction.existsById(id))
            throw new ApiException(HttpStatus.NOT_FOUND, "Transaction not found with id = " + id);
        try
        {
            repoTransaction.deleteById(id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error while deleting transactions");
        }
    }

    /** Filter transactions */
    @Override
    public List<Transaction> findFiltered(Integer clientId, Integer year, Integer month)
    {
        return repoTransaction.findFiltered(clientId, year, month);
    }

    /** Delete filtered transactions */
    @Override
    public int deleteFiltered(Integer clientId, Integer year, Integer month)
    {
        return repoTransaction.deleteFiltered(clientId, year, month);
    }

}
