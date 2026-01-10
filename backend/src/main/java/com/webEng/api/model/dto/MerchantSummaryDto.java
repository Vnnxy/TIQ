package com.webEng.api.model.dto;

/**
 * Dto for the merchant summary
 */
public class MerchantSummaryDto {

    // The merchant id
    private Integer merchant_id;
    // the merchant city
    private String city;
    // The merchant state
    private String state;
    // The total
    private Double total;
    // Number of transactions
    private Long transactions;

    /**
     * Empty constructor
     */
    public MerchantSummaryDto() {
    }

    /**
     * Constructor for the Merchant summary dto
     * 
     * @param merchant_id  The merchant id
     * @param city         The merchant city
     * @param state        The merchant state
     * @param total        The total amount usd of transactions
     * @param transactions The numberr of transactions
     */
    public MerchantSummaryDto(Integer merchant_id, String city, String state, Double total, Long transactions) {
        this.merchant_id = merchant_id;
        this.city = city;
        this.state = state;
        this.total = total;
        this.transactions = transactions;
    }

    /**
     * Getter for the merchant id
     * 
     * @return the merchant id
     */
    public Integer getMerchant_id() {
        return merchant_id;
    }

    /**
     * Setter for the merchant id
     * 
     * @param merchantId the merchant id
     */
    public void setMerchant_id(Integer merchantId) {
        this.merchant_id = merchantId;
    }

    /**
     * Getter for the city
     * 
     * @return the city of the merchant
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter for the city
     * 
     * @param city The city of the merchant
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter for the state of the merchant
     * 
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     * Setter for the state of the merchant
     * 
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter for the total amount usd
     * 
     * @return The total amount
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Setter for the total amount usd
     * 
     * @param total The total amount in usd
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Getter for the number of transactions
     * 
     * @return the number of transactions.
     */
    public Long getTransactions() {
        return transactions;
    }

    /**
     * Setter for the number of transactions
     * 
     * @param transactions the number of transactions
     */
    public void setTransactions(Long transactions) {
        this.transactions = transactions;
    }

}
