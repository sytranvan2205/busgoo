package com.iuh.busgoo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	Promotion findByCode(String code);
	
	List<Promotion> findByFromDateLessThan(Date fromDate);
	
	List<Promotion> findByToDateGreaterThanEqual(Date toDate);
	
	@Query("SELECT p FROM Promotion p WHERE (:startDate IS NULL OR p.fromDate >= :startDate) AND (:endDate IS NULL OR p.toDate <= :endDate)")
    List<Promotion> findPromotionsBetweenDates(Date startDate, Date endDate);
}
