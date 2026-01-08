package com.webEng.api.dto;

import java.time.LocalDateTime;
import com.webEng.api.model.Transaction;

public class TransactionDto
{
    private Integer id;
    private Integer merchantId;
    private LocalDateTime date;
    private Double amount;
    private Integer clientId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getClientId() { return clientId; }
    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public Integer getMerchantId() { return merchantId; }
    public void setMerchantId(Integer merchantId) { this.merchantId = merchantId; }

    public TransactionDto() {};

    public TransactionDto(Transaction tx)
    {
        id = tx.getId();
        merchantId = tx.getMerchant().getId();
        date = tx.getDate();
        amount = tx.getAmount();
        clientId = tx.getClientId();
    }

    /**
     * for update operation, where an ID is found to update.
     *
     * @param id
     * @param dto
     */
    public TransactionDto(Integer id, TransactionWriteDto dto)
    {
        this.id = id;
        clientId = dto.getClientId();
        amount = dto.getAmount();
        date = dto.getDate();
        merchantId = dto.getMerchantId();
    }

    /**
     * for create operation, where id is null for save to make a new one
     * @param dto
     */
    public TransactionDto(TransactionWriteDto dto)
    {
        id = null;
        clientId = dto.getClientId();
        amount = dto.getAmount();
        date = dto.getDate();
        merchantId = dto.getMerchantId();
    }
}
