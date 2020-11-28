package com.betbull.team.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betbull.team.model.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
	
	List<Currency> findByNameContainingIgnoreCase(String name);
	List<Currency> findByIsValid(Boolean isValid);

}
