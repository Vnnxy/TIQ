package com.webEng.api.repository;

import java.util.Locale.Category;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.transaction.Transactional;

import com.webEng.api.model.Transaction;

/**
 * Interface for the Transaction repository.
 */
@Repository
public interface RepoTransaction extends JpaRepository<Transaction, Integer> {

}