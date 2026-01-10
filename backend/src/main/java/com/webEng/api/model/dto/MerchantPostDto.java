package com.webEng.api.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Miguel Akira López Asano
 * 
 *         Class for the Dto used for creating a Merchant
 *         This is required because we are using save method from JPA, which
 *         acts as a put if id is present. Therefore, to avoid dangerous
 *         behaviour, we use this DTO to never send an ID when doing POST.
 */
public class MerchantPostDto {

    /**
     * City where the merchant is based on. If the transaaction was ONLINE,
     * the tag ONLINE will be present instead of the city name.
     */
    @NotBlank(message = "City is required")
    String merchantCity;

    /**
     * State where the merchant is located. If the transaction was online,
     * no state will be provided.
     */
    String merchantState;

    /**
     * EMpty constructor
     */
    public MerchantPostDto() {
    }

    /**
     * Constructor
     * 
     * @param merchantCity  City of the merchant
     * @param merchantState State of the merchant
     */
    public MerchantPostDto(String merchantCity, String merchantState) {
        this.merchantCity = merchantCity;
        this.merchantState = merchantState;
    }

    /**
     * Getter for the city where the merchant is located or if it's online.
     * 
     * @return The city of the merchant or ONLINE tag.
     */
    public String getMerchantCity() {
        return merchantCity;
    }

    /**
     * Setter for the merchant city.
     * 
     * @param merchantCity Name of the city or ONLINE.
     */
    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    /**
     * Getter for the merchant state.
     * 
     * @return The merchant state.
     */
    public String getMerchantState() {
        return merchantState;
    }

    /**
     * Setter for the geographicak state of the merchant.
     * 
     * @param merchantState The state where the merchant is located.
     */
    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }
}
