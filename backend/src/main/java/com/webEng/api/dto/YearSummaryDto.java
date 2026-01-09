package com.webEng.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Dto for the year summary
 */
public class YearSummaryDto {
    // Average amount
    private Double average;
    // Count of transactions
    private Long count;
    // The period(time) wither month or quadrant
    private LocalDate period;
    // Total amount
    private Double total;

    /**
     * Empty constructor
     */
    public YearSummaryDto() {
    }

    /**
     * Constructor for the year summary
     * 
     * @param period  The period of the summary
     * @param average The average amount usd
     * @param count   The number of transactions
     */
    public YearSummaryDto(LocalDateTime period, Double average, Long count, Double total) {
        this.period = period.toLocalDate();
        this.average = average;
        this.count = count;
        this.total = total;
    }

    /**
     * Getter for the average
     * 
     * @return the average
     */
    public Double getAverage() {
        return average;
    }

    /**
     * Setter for the average amount
     * 
     * @param average amount
     */
    public void setAverage(Double average) {
        this.average = average;
    }

    /**
     * Getter for the count of transactions
     * 
     * @return The number of transactions
     */
    public Long getCount() {
        return count;
    }

    /**
     * Setter for the number of transactions
     * 
     * @param count The number of transactions
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * Getter for the periodd
     * 
     * @return the period
     */
    public LocalDate getPeriod() {
        return period;
    }

    /**
     * Setter for the period.
     * 
     * @param period The period
     */
    public void setPeriod(LocalDateTime period) {
        this.period = period.toLocalDate();
    }

    /**
     * Getter for the total usd
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Setter for the total
     * 
     * @param total the total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

}
