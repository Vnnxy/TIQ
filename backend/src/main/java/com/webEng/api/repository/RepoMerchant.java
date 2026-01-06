package com.webEng.api.repository;

import com.webEng.api.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoMerchant extends JpaRepository<Merchant, Integer> {

}