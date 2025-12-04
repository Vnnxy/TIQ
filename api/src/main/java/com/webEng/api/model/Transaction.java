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
    LocalDateTime timestamp;

    /**
     * Amount in USD for the transaction.
     */
    double amount;

    /**
     * Id of the client that created the transaction.
     */
    Integer client_id;

}
