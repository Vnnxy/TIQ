package com.webEng.api.dto;

import java.time.LocalDateTime;

/**
 * @author Miguel Akira López Asano
 * 
 *         Class for the Dto of the Maximum Amount of a transaction.
 */
public class MaximumAmountDto {

    /**
     * The maximum amount of a transaction in USD
     */
    private double maxAmount;
    /**
     * The timestamp for the exact moment of the trransaction
     */
    private LocalDateTime timestamp;
    /**
     * The city of the transaction or if it was online.
     */
    private String location;

    /**
     * Empty constructor
     */
    public MaximumAmountDto() {
    }

    /**
     * Constructor for the dto object
     * 
     * @param maxAmount Integer for the maximum amount in usd for the transaction.
     * @param timestamp LocalDateTime object with the timestamp of the transaaction
     * @param location  String with the city or online.
     */
    public MaximumAmountDto(double maxAmount, LocalDateTime timestamp, String location) {
        this.timestamp = timestamp;
        this.maxAmount = maxAmount;
        this.location = location;
    }

    /**
     * Getter for the max amount in USD
     * 
     * @return The amount of the transaction
     */
    public double getMaxAmount() {
        return maxAmount;
    }

    /**
     * Setter for the amount
     * 
     * @param maxAmount Double with the amount of the transaction
     */
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    /**
     * Getter for the timestamp
     * 
     * @return LocalDateTime object with the timestamp of the transaction.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for the timestamp of the transaction
     * 
     * @param timestamp The LocalDateTime object of the timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter for the location of the transaction
     * 
     * @return String with the city or online.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for the location (city) or online.
     * 
     * @param location String with the city or ONLINE.
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
