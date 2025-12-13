package com.webEng.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

/**
 * @author Miguel Akira López Asano
 *         Java class for the merchant information.
 * 
 */
@Entity
@Table(name = "merchant")
public class Merchant {

    /**
     * Unique identifier for the merchant
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer merchant_id;

    /**
     * City where the merchant is based on. If the transaaction was ONLINE,
     * the tag ONLINE will be present instead of the city name.
     */
    String merchant_city;

    /**
     * State where the merchant is located. If the transaction was online,
     * no state will be provided.
     */
    String merchant_state;

    /**
     * Getter for the identifier of the merchant
     * 
     * @return integer representing the id of the merchant.
     */
    public Integer getMerchantId() {
        return merchant_id;
    }

    /**
     * Setter for the merchant id.
     * 
     * @param merchantId Integer id of the merchant
     */
    public void setMerchantId(Integer merchantId) {
        this.merchant_id = merchantId;
    }

    /**
     * Getter for the city where the merchant is located or if it's online.
     * 
     * @return The city of the merchant or ONLINE tag.
     */
    public String getMerchantCity() {
        return merchant_city;
    }

    /**
     * Setter for the merchant city.
     * 
     * @param merchantCity Name of the city or ONLINE.
     */
    public void setMerchantCity(String merchantCity) {
        this.merchant_city = merchantCity;
    }

    /**
     * Getter for the merchant state.
     * 
     * @return The merchant state.
     */
    public String getMerchantState() {
        return merchant_state;
    }

    /**
     * Setter for the geographicak state of the merchant.
     * 
     * @param merchantState The state where the merchant is located.
     */
    public void setMerchantState(String merchantState) {
        this.merchant_state = merchantState;
    }
}
