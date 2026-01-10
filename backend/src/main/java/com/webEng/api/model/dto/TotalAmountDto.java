package com.webEng.api.model.dto;

import java.time.LocalDate;

/**
 * Class for the total amount returned per day DTO.
 * 
 * @author Miguel Akira López Asano
 *         This class helps encapsulate the data into an object with the desired
 *         data.
 */
public class TotalAmountDto {
    /**
     * The day of the transaction.
     */
    private LocalDate day;
    /**
     * The total amount for the day in USD
     */
    private double totalAmount;

    /**
     * Constructor for the dto object
     * 
     * @param day          The day of the transaction
     * @param total_amount Total amount of the day. (USD)
     */
    public TotalAmountDto(LocalDate day, double total_amount) {
        this.day = day;
        this.totalAmount = total_amount;
    }

    /**
     * Empty contructor for TotalAmountDto
     */
    public TotalAmountDto() {
    }

    /**
     * Getter for the day.
     * 
     * @return LocalDate object for the day.
     */
    public LocalDate getDay() {
        return day;
    }

    /**
     * Setter for the day of the total.
     * 
     * @param day LocalDate object with the day.
     */
    public void setDay(LocalDate day) {
        this.day = day;
    }

    /**
     * Getter for the total amount in USD
     * 
     * @return The total amount (Double) for the given day.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Setter for the totalAmount of the day
     * 
     * @param totalAmount double with the total amount in usd
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
