package com.webEng.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class YearSummaryDto {
    private Double average;

    private Long count;

    private LocalDate period;

    public YearSummaryDto() {
    }

    public YearSummaryDto(LocalDateTime period, Double average, Long count) {
        this.period = period.toLocalDate();
        this.average = average;
        this.count = count;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDateTime period) {
        this.period = period.toLocalDate();
    }

}
