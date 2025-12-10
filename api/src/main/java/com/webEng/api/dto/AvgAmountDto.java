package com.webEng.api.dto;

public class AvgAmountDto {

    /**
     * Avg amount of the transaction
     */
    private Double avgAmount;

    public AvgAmountDto() {
    }

    public AvgAmountDto(Double avgAmount) {
        this.avgAmount = avgAmount;
    }

    public Double getAvgAmount() {
        return avgAmount;
    }

    public void setAvgAmount(Double avgAmount) {
        this.avgAmount = avgAmount;
    }
}
