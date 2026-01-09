package com.webEng.api.dto;

/**
 * Dto for the state summary
 */
public class StateSummaryDto {

    // The merchant state
    private String merchantState;
    // The total amount
    private Double total;

    /**
     * Empty constructor
     */
    public StateSummaryDto() {
    }

    /**
     * Constructor for the dto
     * 
     * @param merchantState The state of the merchant
     * @param total         The total amount
     */
    public StateSummaryDto(String merchantState, Double total) {
        this.merchantState = merchantState;
        this.total = total;
    }

    /**
     * Getter for the merchant state
     * 
     * @return The merchant state
     */
    public String getMerchantState() {
        return merchantState;
    }

    /**
     * Setter for the merchant state
     * 
     * @param merchantState
     */
    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }

    /**
     * Getter for the total amount
     * 
     * @return the total amount
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Setter for the total amount
     * 
     * @param total The total amount
     */
    public void setTotal(Double total) {
        this.total = total;
    }
}
