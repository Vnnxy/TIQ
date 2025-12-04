package com.webEng.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

/**
 * @author Miguel Akira López Asano
 *         Java class for the merchant information.
 * 
 */
@Entity
@Table(name = "merchant")
public class Merchant {

    /**
     * Unique identifier for the merchant
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer merchant_id;

    /**
     * City where the merchant is based on. If the transaaction was ONLINE,
     * the tag ONLINE will be present instead of the city name.
     */
    String merchant_city;

    /**
     * State where the merchant is located. If the transaction was online,
     * no state will be provided.
     */
    String merchant_state;
}
