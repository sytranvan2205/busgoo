package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
//	List<Price> findByRouteIdAndTypeBusId(Long routeId, Long typeBusId);
	
	@Query(value = "select * from price where status = 1 "
			+ "and from_date >= :fromDate "
			+ "and to_date <= :toDate "
			+ "and status = 1 ",nativeQuery = true)
	List<Price> getLstByFromDateAndToDate(@Param("fromDate")LocalDate fromDate,@Param("toDate") LocalDate toDate);
	
	@Query(value = "select p from Price p where "
			+ "(:status is null or p.status = :status) "
			+ "and (:fromDate is null or p.fromDate >= :fromDate ) "
			+ "and (:toDate is null or p.toDate <= :toDate ) ")
	Page<Price> findByStatusAndFromDateGreaterThanEqualAndToDateLessThanEqual(Integer status, LocalDate fromDate, LocalDate toDate, Pageable pageable);
	
	Page<Price> findByFromDateGreaterThanEqual(LocalDate fromDate,Pageable pageable);
	
	Page<Price> findByStatus(Integer status, Pageable pageable);
	
	Page<Price> findByToDateLessThanEqual(LocalDate toDate,Pageable pageable);
	
}
