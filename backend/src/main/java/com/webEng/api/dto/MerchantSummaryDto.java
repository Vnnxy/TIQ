package com.webEng.api.dto;

public class MerchantSummaryDto {

    private Integer merchant_id;

    private String city;

    private String state;

    private Double total;

    private Long transactions;

    public MerchantSummaryDto() {
    }

    public MerchantSummaryDto(Integer merchant_id, String city, String state, Double total, Long transactions) {
        this.merchant_id = merchant_id;
        this.city = city;
        this.state = state;
        this.total = total;
        this.transactions = transactions;
    }

    public Integer getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(Integer merchantId) {
        this.merchant_id = merchantId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getTransactions() {
        return transactions;
    }

    public void setTransactions(Long transactions) {
        this.transactions = transactions;
    }

}
