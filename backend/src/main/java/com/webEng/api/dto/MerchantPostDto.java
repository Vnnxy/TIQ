package com.webEng.api.dto;

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
        this.merchant_city = merchantCity;
        this.merchant_state = merchantState;
    }

    /**
     * City where the merchant is based on. If the transaaction was ONLINE,
     * the tag ONLINE will be present instead of the city name.
     */
    @NotBlank(message = "City is required")
    String merchant_city;

    /**
     * State where the merchant is located. If the transaction was online,
     * no state will be provided.
     */
    @NotBlank(message = "State is required")
    String merchant_state;

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
