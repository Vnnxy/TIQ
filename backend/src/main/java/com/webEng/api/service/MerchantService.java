package com.webEng.api.service;

import java.util.List;

import com.webEng.api.model.Merchant;

public interface MerchantService {

    /**
     * Gets a merchant by an id
     * 
     * @param id Id of the merchant
     * @return The merchant
     */
    Merchant getMerchant(Integer id);

    /**
     * Saves a merchant
     * 
     * @param merchant Merchant to save
     * @return The saved merchant
     */
    Merchant saveMerchant(Merchant merchant);

    /**
     * Gets all merchants
     * 
     * @param limit The limit of merchants
     * @param page  page we start on
     * 
     * @return List containing all merchants
     */
    List<Merchant> getMerchants(Integer limit, Integer page);

    /**
     * Updates a merchant
     * 
     * @param merchant
     * @return Updated merchant
     */
    Merchant updateMerchant(Merchant merchant);

    /**
     * Deletes a merchant
     * 
     * @param id Id of the merchant to be deleted
     */
    void deleteMerchant(Integer id);

    /**
     * VErifies if a merchant exists
     * 
     * @param id Id of the merchant
     * @return True if the merchant exists, false otherwise
     */
    boolean existsById(Integer id);
}
