package com.webEng.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.webEng.api.model.Transaction;
import com.webEng.api.model.dto.ClientSummaryDto;
import com.webEng.api.model.dto.MerchantSummaryDto;
import com.webEng.api.model.dto.StateSummaryDto;
import com.webEng.api.model.dto.YearSummaryDto;

/**
 * Interface for the Summary repository.
 * 
 * @author Miguel Akira López Asano
 */
public interface RepoSummary extends JpaRepository<Transaction, Integer> {
    /**
     * Obtains the summary of the clients transactions and amounts
     * 
     * @param year   The year filter
     * @param month  Month filter
     * @param view   Use transaction_count for ordering by Number of transactions,
     *               use total_spent for amount, avg for average.
     * @param limit  limit of the response
     * @param offset offset
     * @return List containing the Client summaries.
     */
    @Query(value = """
            SELECT
                t.client_id,
                SUM(t.amount) AS total,
                COUNT(t.id) AS count,
                AVG(t.amount) AS avg
            FROM transaction t
            WHERE (:year IS NULL OR EXTRACT(YEAR FROM t.date) = :year )
            AND (:month IS NULL OR EXTRACT(MONTH FROM t.date) = :month)
            GROUP BY t.client_id
            ORDER BY
                CASE WHEN :view = 'total' THEN SUM(t.amount) END DESC,
                CASE WHEN :view = 'count' THEN COUNT(t.id) END DESC,
                CASE WHEN :view = 'avg' THEN AVG(t.amount) END DESC
            LIMIT :limit
            OFFSET :offset;
            """, nativeQuery = true)
    List<ClientSummaryDto> getClientSummary(@Param("year") Integer year, @Param("month") Integer month,
            @Param("view") String view,
            @Param("limit") Integer limit, @Param("offset") Integer offset);

    /**
     * Gets the merchant summary with the applied filters
     * 
     * @param year   Year filter, If null it will ignore Year.
     * @param month  Month filter, If null it will ignore Month.
     * @param state  State filter, If null it will ignore the state.
     * @param city   City filter, If null it will ignore City.
     * @param limit  Limit of the results
     * @param offset Offset for the results
     * @return List containing the MerchantSummary.
     */
    @Query(value = """
                SELECT
                    m.merchant_id,
                    m.merchant_city,
                    m.merchant_state,
                    SUM(t.amount) AS total,
                    COUNT(t.id) AS transaction_count
                FROM transaction t
                JOIN merchant m ON t.merchant_id = m.merchant_id
                WHERE
                    (:state IS NULL OR m.merchant_state ILIKE :state)
                    AND (:city IS NULL OR m.merchant_city ILIKE :city)
                    AND (:year IS NULL OR EXTRACT(YEAR FROM t.date) = :year )
                    AND (:month IS NULL OR EXTRACT(MONTH FROM t.date) = :month)
                GROUP BY m.merchant_id, m.merchant_city, m.merchant_state
                ORDER BY total DESC
                LIMIT :limit
                OFFSET :offset;
            """, nativeQuery = true)
    public List<MerchantSummaryDto> getMerchantSummary(@Param("year") Integer year, @Param("month") Integer month,
            @Param("state") String state, @Param("city") String city,
            @Param("limit") Integer limit, @Param("offset") Integer offset);

    /**
     * Gets the state data summary
     * 
     * @param year  Year filter, If null it will ignore Year.
     * @param month Month filter, If null it will ignore Month.
     * @return List containing the StateSummary
     */
    @Query(value = """
            SELECT
                m.merchant_state,
                SUM(t.amount) as total
            FROM transaction t
            JOIN merchant m ON t.merchant_id = m.merchant_id
            WHERE (:year IS NULL OR EXTRACT(YEAR FROM t.date) = :year )
                AND (:month IS NULL OR EXTRACT(MONTH FROM t.date) = :month)
            GROUP BY m.merchant_state
            ORDER BY total DESC;
                """, nativeQuery = true)
    public List<StateSummaryDto> getStateSummary(@Param("year") Integer year, @Param("month") Integer month);

    /**
     * Gets the yearly summary by period
     * 
     * @param start  The start year of the report
     * @param end    End year of the report
     * @param detail detail for a monthly view and quarter for a quarter view.
     * @return List containing the year summary.
     */
    @Query(value = """
                        SELECT
                DATE_TRUNC(:detail, t.date) AS period,
                AVG(t.amount) AS average_amount,
                COUNT(t.id) AS transaction_count,
                SUM(t.amount) AS total_amount
            FROM transaction t
            WHERE EXTRACT(YEAR FROM t.date) BETWEEN :start AND :end
            GROUP BY period
            ORDER BY period
                        """, nativeQuery = true)
    public List<YearSummaryDto> getYearSummaryByPeriod(@Param("start") Integer start, @Param("end") Integer end,
            @Param("detail") String detail);

}
