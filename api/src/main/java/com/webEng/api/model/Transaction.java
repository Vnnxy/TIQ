package com.webEng.api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * @author Miguel Akira López Asano
 *         Class for the transaction table
 */
@Entity
@Table(name = "transaction")
public class Transaction {

    /**
     * Unique identifier for the Transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    /**
     * Merchant data
     * This represents the foreign key that references to the merchant table.
     */
    @ManyToOne
    @JoinColumn(name = "merchant_id")
    Merchant merchant;

    /**
     * Date of the transaction. This contains YYYY-MM-DD T HH:MM:SS following the
     * ISO-8601 calendar system.
     */
    LocalDateTime date;

    /**
     * Amount in USD for the transaction.
     */
    double amount;

    /**
     * Id of the client that created the transaction.
     */
    Integer client_id;

    /**
     * Getter for the id of the transaction.
     * 
     * @return Integer representing the id of the transaction
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter for the id of the transaction.
     * 
     * @param id Identifier of the transaction.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter for the merchant
     * 
     * @return The merchant associated to the transaction.
     */
    public Merchant getMerchant() {
        return merchant;
    }

    /**
     * Setter for the merchant of the transaction.
     * 
     * @param merchant Merchant that is associated with the transaction.
     */
    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    /**
     * Getter for the date.
     * 
     * @return LocalDateTime object representing the date and time of the
     *         transaction.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setter for the date
     * 
     * @param timestamp The Date and time of the transaction.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.date = timestamp;
    }

    /**
     * Getter for the amount of the transaction (USD)
     * 
     * @return The amount of the transaction.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for the amount of the transaction
     * 
     * @param amount The amount.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter of the identifier of the client involved in the transaction.
     * 
     * @return The id of the client.
     */
    public Integer getClientId() {
        return client_id;
    }

    /**
     * Setter for the id of the client.
     * 
     * @param clientId Id of the client
     */
    public void setClientId(Integer clientId) {
        this.client_id = clientId;
    }

}
