package com.webEng.api.dto;

import com.webEng.api.model.Transaction;

import java.time.LocalDateTime;


/**
 * Transaction write DTO for post and put operations
 */
public class TransactionWriteDto
{
    private Integer merchantId;
    private LocalDateTime date;
    private Double amount;
    private Integer clientId;

    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getMerchantId() { return merchantId; }
    public void setMerchantId(Integer merchantId) { this.merchantId = merchantId; }

    public TransactionWriteDto() {};

    public TransactionWriteDto(Transaction tx)
    {
        merchantId = tx.getMerchant().getId();
        date = tx.getDate();
        amount = tx.getAmount();
        clientId = tx.getClientId();
    }
}
