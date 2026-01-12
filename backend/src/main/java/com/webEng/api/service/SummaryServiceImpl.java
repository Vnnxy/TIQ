package com.webEng.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.webEng.api.exception.ApiException;
import com.webEng.api.model.dto.ClientSummaryDto;
import com.webEng.api.model.dto.MerchantSummaryDto;
import com.webEng.api.model.dto.StateSummaryDto;
import com.webEng.api.model.dto.YearSummaryDto;
import com.webEng.api.repository.RepoSummary;

@Service
public class SummaryServiceImpl implements SummaryService {

    @Autowired
    RepoSummary repoSummary;

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
    @Override
    public List<ClientSummaryDto> getClientSummary(Integer year, Integer month, String view, Integer limit,
            Integer offset) {
        if (!view.equals("count") && !view.equals("avg") && !view.equals("total"))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please use a valid view: total, avg or count");
        try {
            return repoSummary.getClientSummary(year, month, view, limit, offset);
        } catch (Exception e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving from the database " + e.getMessage());
        }
    }

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
    @Override
    public List<MerchantSummaryDto> getMerchantSummary(Integer year, Integer month, String state, String city,
            Integer limit, Integer offset) {
        return repoSummary.getMerchantSummary(year, month, state, city, limit, offset);

    }

    /**
     * Gets the summaries for the locations
     * 
     * @param year  Year filter, If null it will ignore Year.
     * @param month Month filter, If null it will ignore Month.
     * @return List containing the StateSummary
     */
    @Override
    public List<StateSummaryDto> getStateSummary(Integer year, Integer month) {
        return repoSummary.getStateSummary(year, month);
    }

    /**
     * Gets the yearly summary
     * 
     * @param start  The start year of the report
     * @param end    End year of the report
     * @param detail detail for a monthly view and quarter for a quarter view.
     * @return List containing the year summary.
     */
    @Override
    public List<YearSummaryDto> getYearSummary(Integer year, Integer end, String detail) {
        if (!detail.equals("month") && !detail.equals("quarter"))
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Bad request, please ude month or quarter as the detail parameter");
        return repoSummary.getYearSummaryByPeriod(year, end, detail);
    }
}
