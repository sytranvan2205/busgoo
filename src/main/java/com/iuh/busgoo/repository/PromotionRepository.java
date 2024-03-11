package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

	Promotion findByCodeAndStatus(String code,Integer status);
	
	Promotion findByIdAndStatus(Long promotionId, Integer status);
	
	List<Promotion> findByFromDateLessThan(Date fromDate);
	
	List<Promotion> findByToDateGreaterThanEqual(Date toDate);
	
	@Query("SELECT p FROM Promotion p WHERE (:startDate IS NULL OR p.fromDate >= :startDate) AND (:endDate IS NULL OR p.toDate <= :endDate)")
    List<Promotion> findPromotionsBetweenDates(Date startDate, Date endDate);
	
	@Query("select p from Promotion p where "
			+ "(:status is null or p.status = :status) "
			+ "and (:code is null or p.code = :code) "
			+ "and (:fromDate is null or p.fromDate >= :fromDate ) "
			+ "and (:toDate is null or p.toDate <= :toDate ) ")
	Page<Promotion> findByStatusAndCodeAndFromDateAndToDate(Integer status, String code, LocalDate fromDate, LocalDate toDate, Pageable pageable);
}
