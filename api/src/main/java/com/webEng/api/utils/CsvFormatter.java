package com.webEng.api.utils;

import com.webEng.api.dto.AvgAmountDto;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import java.util.List;

/**
 * Class handling the conversion from json to csv
 */
public class CsvFormatter {

    /**
     * Formats the avg Amount dto into csv representation
     * 
     * @param aad AvgAmountDto object.
     * @return Csv representation of the AvgAmountDto
     */
    public String avgAmountToCsv(AvgAmountDto aad) {
        StringBuffer sb = new StringBuffer();
        sb.append("avgAmount\n");
        sb.append(aad.getAvgAmount());
        return sb.toString();
    }

    /**
     * Formats the total amount into csv representation.
     * 
     * @param list List containing TotalAmountDto
     * @return Csv representation of the list.
     */
    public String totalAmountToCsv(List<TotalAmountDto> list) {
        StringBuffer sb = new StringBuffer();
        sb.append("day,totalAmount\n");
        for (TotalAmountDto tad : list) {
            sb.append(tad.getDay()).append(",").append(tad.getTotalAmount()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats the max amount into csv representation.
     * 
     * @param list List containing MaxAmountDto
     * @return Csv representation of the list.
     */
    public String maxAmountToCsv(List<MaximumAmountDto> list) {
        StringBuffer sb = new StringBuffer();
        sb.append("maxAmount, timestamp, location");
        for (MaximumAmountDto mad : list) {
            sb.append(mad.getMaxAmount()).append(mad.getTimestamp()).append(mad.getLocation()).append("\n");
        }
        return sb.toString();
    }
}
