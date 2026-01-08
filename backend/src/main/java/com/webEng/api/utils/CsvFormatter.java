package com.webEng.api.utils;

import com.webEng.api.dto.AvgAmountDto;
import com.webEng.api.dto.ClientSummaryDto;
import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.MerchantSummaryDto;
import com.webEng.api.dto.StateSummaryDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.dto.YearSummaryDto;

import java.util.List;
import com.webEng.api.exception.*;
import com.webEng.api.model.Merchant;;

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

    /**
     * Formats the exceptions to csv representation.
     * 
     * @param response The exception we want to format
     * @return Csv representation of the exception
     */
    public String apiExceptionToCsv(ExceptionResponse response) {
        StringBuffer sb = new StringBuffer("timestamp,status,error,message,path\n");
        sb.append(response.getTimestamp()).append(",");
        sb.append(response.getStatus()).append(",");
        sb.append(response.getError()).append(",");
        sb.append(response.getMessage()).append(",");
        sb.append(response.getPath());
        return sb.toString();
    }

    /**
     * Formats merchants into csv
     * 
     * @param merchant The merchant to format
     * @return Formatted string in csv representation.
     */
    public String merchantToCsv(Merchant merchant) {
        StringBuffer sb = new StringBuffer("merchant_id,merchant_city,merchant_state\n");
        sb.append(merchant.getId()).append(",");
        sb.append(merchant.getMerchantCity()).append(",");
        sb.append(merchant.getMerchantState());
        return sb.toString();
    }

    /**
     * Formats a list of merchants into csv
     * 
     * @param merchants List containing merchants
     * @return Formatted string in csv.
     */
    public String merchantListToCsv(List<Merchant> merchants) {
        StringBuffer sb = new StringBuffer();
        sb.append("merchant_id,merchant_city,merchant_state\n");
        for (Merchant merchant : merchants) {
            sb.append(merchant.getId()).append(",").append(merchant.getMerchantCity()).append(",")
                    .append(merchant.getMerchantState()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a list of client summary into csv
     * 
     * @param summary List containing client summary
     * @return Formatted string in csv
     */
    public String clientSummaryToCsv(List<ClientSummaryDto> summary) {
        StringBuffer sb = new StringBuffer();
        sb.append("client_id,total,transaction_count,average_transaction\n");
        for (ClientSummaryDto csd : summary) {
            sb.append(csd.getClientId()).append(",").append(csd.getTotal()).append(",")
                    .append(csd.getTransactionCount()).append(",").append(csd.getAverageTransaction()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a list of merchant summary into csv
     * 
     * @param summary List containing merchant summary
     * @return Formatted string in csv
     */
    public String merchantSummaryToCsv(List<MerchantSummaryDto> summary) {
        StringBuffer sb = new StringBuffer();
        sb.append("merchant_id,city,state,total,transactions\n");
        for (MerchantSummaryDto csd : summary) {
            sb.append(csd.getMerchant_id()).append(",").append(csd.getCity()).append(",")
                    .append(csd.getState()).append(",").append(csd.getTotal()).append(",").append(csd.getTransactions())
                    .append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a list of state summary into csv
     * 
     * @param summary List containing state summary
     * @return Formatted string in csv
     */
    public String stateSummaryToCsv(List<StateSummaryDto> summary) {
        StringBuffer sb = new StringBuffer();
        sb.append("merchant_state,total\n");
        for (StateSummaryDto csd : summary) {
            sb.append(csd.getMerchantState()).append(",").append(csd.getTotal()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Formats a list of state summary into csv
     * 
     * @param summary List containing year summary
     * @return Formatted string in csv
     */
    public String yearSummaryToCsv(List<YearSummaryDto> summary) {
        StringBuffer sb = new StringBuffer();
        sb.append("period,average,count\n");
        for (YearSummaryDto csd : summary) {
            sb.append(csd.getPeriod()).append(",").append(csd.getAverage()).append(",").append(csd.getCount())
                    .append("\n");
        }
        return sb.toString();
    }
}
