package com.webEng.api.dto;

public class AvgAmountDto {

    /**
     * Avg amount of the transaction
     */
    private Double avgAmount;

    /**
     * Empty constructor
     */
    public AvgAmountDto() {
    }

    /**
     * Contstructor for the avgAmount
     * 
     * @param avgAmount The average amount
     */
    public AvgAmountDto(Double avgAmount) {
        this.avgAmount = avgAmount;
    }

    /**
     * Getter for the avg amount
     * 
     * @return Double containing the avg amount
     */
    public Double getAvgAmount() {
        return avgAmount;
    }

    /**
     * Setter for the avg amount
     * 
     * @param avgAmount The avg amount.
     */
    public void setAvgAmount(Double avgAmount) {
        this.avgAmount = avgAmount;
    }
}
