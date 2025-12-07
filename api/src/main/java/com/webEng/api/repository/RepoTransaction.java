package com.webEng.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.webEng.api.dto.MaximumAmountDto;
import com.webEng.api.dto.TotalAmountDto;
import com.webEng.api.model.Transaction;

/**
 * Interface for the Transaction repository.
 * 
 * @author Miguel Akira López Asano
 */
@Repository
public interface RepoTransaction extends JpaRepository<Transaction, Integer> {

        /**
         * Gets the average amount of processed transactions for a given city, year and
         * optionally a month.
         * 
         * Req:
         * to retrieve the average amount per processed transaction (in USD) in a
         * given city per month and year, excluding online transactions;
         * 
         * @param city  Required parameter, the name of the city.
         * @param year  Required parameter, the year (Integer) of the transaction.
         * @param month Optional parameter, the month(Integer) of the transaction.
         */
        @Query(value = """
                        SELECT avg(t.amount)
                        FROM transaction t
                        JOIN merchant m
                            ON t.merchant_id = m.merchant_id
                        WHERE m.merchant_city = :city
                          AND EXTRACT(YEAR FROM t.date) = :year
                          AND (:month IS NULL OR EXTRACT(MONTH FROM t.date) = :month)
                        """, nativeQuery = true)
        Double getAvgAmount(@Param("city") String city, @Param("year") Integer year, @Param("month") Integer month);

        /**
         * Gets the total amount of processed transactions in USD for a given state and
         * month.
         * 
         * Req:to retrieve the total amount in process transactions (in USD) per day
         * at a given US state and for a given calendar month, returned in batches of M=
         * {10, 20, 50, 100};
         * 
         * @param state  Required parameter, String containing the state of the
         *               merchant.
         * @param month  Required parameter, Integer representing the month.
         * @param limit  Required parameter, Integer with the size of the batch.
         * @param offset Optional parameter, offset for the results.
         * @return
         */
        @Query(value = """
                        SELECT DATE(transaction.date) AS day,
                        SUM(transaction.amount) AS total_amount
                        FROM transaction
                        JOIN merchant
                                ON transaction.merchant_id = merchant.merchant_id
                        WHERE merchant_state = :state
                                AND EXTRACT(MONTH FROM transaction.date) = :month
                        GROUP BY DATE(transaction.date)
                        ORDER BY day
                        LIMIT :limit
                        OFFSET :offset;
                        """, nativeQuery = true)
        List<TotalAmountDto> getTotalAmount(@Param("state") String state, @Param("month") Integer month,
                        @Param("limit") Integer limit, @Param("offset") Integer offset);

        /**
         * Retrieves the top maximum transactions for the given parameters.
         * 
         * req:to retrieve the top or bottom N, N ≥1 maximum transaction amounts
         * across all data either for a specific year or for the previous M, M ≥1 years,
         * including the information about the specific moment (read: timestamp)
         * and the city where this transaction took place or whether this transaction
         * took place online
         * 
         * @param start  Left bound for the year (Inclusive)
         * @param end    Right bound for the year (Inclusive), if its a single year,
         *               start = end
         * @param limit  Limit for the batch
         * @param offset Offset for the results
         * @return List containing the top maximum values and the relevant information.
         */
        @Query(value = """
                        SELECT
                                t.date,
                                t.amount,
                                m.merchant_city
                                FROM transaction t
                                JOIN merchant m ON t.merchant_id = m.merchant_id
                                WHERE EXTRACT(YEAR FROM t.date) BETWEEN :start AND :end
                                ORDER BY t.amount DESC
                                LIMIT :limit
                                OFFSET :offset;
                        """, nativeQuery = true)
        List<MaximumAmountDto> getMaximumAmountTop(@Param("start") Integer start, @Param("end") Integer end,
                        @Param("limit") Integer limit, @Param("offset") Integer offset);

        /**
         * Retrieves the bottom maximum transactions for the given parameters.
         * 
         * req:to retrieve the top or bottom N, N ≥1 maximum transaction amounts
         * across all data either for a specific year or for the previous M, M ≥1 years,
         * including the information about the specific moment (read: timestamp)
         * and the city where this transaction took place or whether this transaction
         * took place online
         * 
         * @param start  Left bound for the year (Inclusive)
         * @param end    Right bound for the year (Inclusive), if its a single year,
         *               start = end
         * @param limit  Limit for the batch
         * @param offset Offset for the results
         * @return List containing the bottom maximum values and the relevant
         *         information.
         */
        @Query(value = """
                        SELECT
                                t.date,
                                t.amount,
                                m.merchant_city
                                FROM transaction t
                                JOIN merchant m ON t.merchant_id = m.merchant_id
                                WHERE EXTRACT(YEAR FROM t.date) BETWEEN :start AND :end
                                ORDER BY t.amount ASC
                                LIMIT :limit
                                OFFSET :offset;
                        """, nativeQuery = true)
        List<MaximumAmountDto> getMaximumAmountBottom(@Param("start") Integer start, @Param("end") Integer end,
                        @Param("limit") Integer limit, @Param("offser") Integer offset);

}