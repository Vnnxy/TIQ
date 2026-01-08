package com.webEng.api.service;

import java.util.List;

import com.webEng.api.dto.ClientSummaryDto;
import com.webEng.api.dto.MerchantSummaryDto;
import com.webEng.api.dto.StateSummaryDto;
import com.webEng.api.dto.YearSummaryDto;

public interface SummaryService {

    /**
     * Gets the summaries of the clients.
     * 
     * @param year   The year filter
     * @param month  Month filter
     * @param view   The view filter.
     * @param limit  limit of the response
     * @param offset offset
     * @return List of the Clients summaries
     */
    public List<ClientSummaryDto> getClientSummary(Integer year, Integer month, String view, Integer limit,
            Integer offset);

    /**
     * Gets the summaries of the merchants
     * 
     * @param year   Year filter, If null it will ignore Year.
     * @param month  Month filter, If null it will ignore Month.
     * @param state  State filter, If null it will ignore the state.
     * @param city   City filter, If null it will ignore City.
     * @param limit  Limit of the results
     * @param offset Offset for the results
     * @return List containing the MerchantSummary.
     */
    public List<MerchantSummaryDto> getMerchantSummary(Integer year, Integer month, String state, String city,
            Integer limit, Integer offset);

    /**
     * Gets the summaries for the locations
     * 
     * @param year  Year filter, If null it will ignore Year.
     * @param month Month filter, If null it will ignore Month.
     * @return List containing the StateSummary
     */
    public List<StateSummaryDto> getStateSummary(Integer year, Integer month);

    /**
     * Gets the yearly summary
     * 
     * @param start  The start year of the report
     * @param end    End year of the report
     * @param detail detail for a monthly view and quarter for a quarter view.
     * @return List containing the year summary.
     */
    public List<YearSummaryDto> getYearSummary(Integer year, Integer end, String detail);

}
