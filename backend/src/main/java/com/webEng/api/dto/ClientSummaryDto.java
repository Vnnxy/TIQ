package com.webEng.api.dto;

/**
 * Dto for the client summary
 */
public class ClientSummaryDto {

    private double total;

    private Long transactionCount;

    private Double averageTransaction;

    private Integer client_id;

    public ClientSummaryDto() {
    }

    public ClientSummaryDto(Integer client_id, double total, Long transactionCount, Double averageTransaction) {
        this.client_id = client_id;
        this.total = total;
        this.transactionCount = transactionCount;
        this.averageTransaction = averageTransaction;

    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public Double getAverageTransaction() {
        return averageTransaction;
    }

    public void setAverageTransactionValue(Double averageTransaction) {
        this.averageTransaction = averageTransaction;
    }

    public Integer getClientId() {
        return client_id;
    }

    public void setClientId(Integer client_id) {
        this.client_id = client_id;
    }

}
