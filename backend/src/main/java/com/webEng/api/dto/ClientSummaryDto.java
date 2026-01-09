package com.webEng.api.dto;

/**
 * Dto for the client summary
 */
public class ClientSummaryDto {

    // The total amount in usd
    private double total;
    // The number of transactions
    private Long transactionCount;
    // The average of transactions
    private Double averageTransaction;
    // The client id
    private Integer client_id;

    /**
     * Empty constructor
     */
    public ClientSummaryDto() {
    }

    /**
     * Constructor for the client summary dto
     * 
     * @param client_id          The id of the client
     * @param total              The total amount in usd
     * @param transactionCount   The number of transactions
     * @param averageTransaction The average number usd
     */
    public ClientSummaryDto(Integer client_id, double total, Long transactionCount, Double averageTransaction) {
        this.client_id = client_id;
        this.total = total;
        this.transactionCount = transactionCount;
        this.averageTransaction = averageTransaction;

    }

    /**
     * Getter for the total amount
     * 
     * @return The total amount
     */
    public double getTotal() {
        return total;
    }

    /**
     * Setter for the total amount
     * 
     * @param total The total amount usd
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Getter for the number of transactions
     */
    public Long getTransactionCount() {
        return transactionCount;
    }

    /**
     * Setter for the number of transactions
     * 
     * @param transactionCount
     */
    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    /**
     * Getter for the average transacctions
     * 
     * @return The average transa tions
     */
    public Double getAverageTransaction() {
        return averageTransaction;
    }

    /**
     * Setter for the average transactions
     * 
     * @param averageTransaction
     */
    public void setAverageTransactionValue(Double averageTransaction) {
        this.averageTransaction = averageTransaction;
    }

    /**
     * Getter for the client id
     * 
     * @return the client id
     */
    public Integer getClientId() {
        return client_id;
    }

    /**
     * Setter for the client id
     * 
     * @param client_id the clinent id.
     */
    public void setClientId(Integer client_id) {
        this.client_id = client_id;
    }

}
