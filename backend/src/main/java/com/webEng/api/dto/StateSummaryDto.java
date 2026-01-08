package com.webEng.api.dto;

public class StateSummaryDto {

    private String merchantState;

    private Double total;

    public StateSummaryDto() {
    }

    public StateSummaryDto(String merchantState, Double total) {
        this.merchantState = merchantState;
        this.total = total;
    }

    public String getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
