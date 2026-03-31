package com.webEng.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.webEng.api.exception.ApiException;
import com.webEng.api.model.Merchant;
import com.webEng.api.repository.RepoMerchant;

@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    RepoMerchant repoMerchant;

    /**
     * Gets a merchant by an id
     * 
     * @param id Id of the merchant
     * @return The merchant
     */
    public Merchant getMerchant(Integer id) {
        if (id == null)
            throw new ApiException(HttpStatus.BAD_REQUEST, "id is a required field");
        return repoMerchant.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Transaction not found with id = " + id));
    }

    /**
     * Saves a merchant
     * 
     * @param merchant Merchant to save
     * @return The saved merchant
     */
    @Override
    public Merchant saveMerchant(Merchant merchant) {
        return repoMerchant.save(merchant);
    }

    /**
     * Gets all merchants
     * 
     * @param limit The limit of merchants
     * @param page  page we start on
     * 
     * @return List containing all merchants
     */
    @Override
    public List<Merchant> getMerchants(Integer limit, Integer page) {
        return repoMerchant.findAll(PageRequest.of(page, limit, Sort.by("id").ascending())).getContent();
    }

    /**
     * Updates a merchant
     * 
     * @param merchant
     * @return Updated merchant
     */
    @Override
    public Merchant updateMerchant(Merchant merchant) {
        return repoMerchant.save(merchant);
    }

    /**
     * Deletes a merchant
     * 
     * @param id Id of the merchant to be deleted
     * @return Deleted merchant
     */
    @Override
    public void deleteMerchant(Integer id) {
        repoMerchant.deleteById(id);
    }

    /**
     * VErifies if a merchant exists
     * 
     * @param id Id of the merchant
     * @return True if the merchant exists, false otherwise
     */
    public boolean existsById(Integer id) {
        return repoMerchant.existsById(id);
    }
}
