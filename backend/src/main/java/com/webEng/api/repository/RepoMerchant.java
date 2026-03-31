package com.webEng.api.repository;

import com.webEng.api.model.Merchant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoMerchant extends JpaRepository<Merchant, Integer> {
    /**
     * Finds a merchant based on its external merchant id (original csv id)
     * 
     * @param externalMerchantId External(csv) merchant id
     * @return Optional object containing a merchant or not.
     */
    Optional<Merchant> findByExternalMerchantId(Integer externalMerchantId);
}