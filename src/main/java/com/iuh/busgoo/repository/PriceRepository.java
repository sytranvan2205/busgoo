package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

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
			+ "and to_date <= :toDate ",nativeQuery = true)
	List<Price> getLstByFromDateAndToDate(@Param("fromDate")LocalDate fromDate,@Param("toDate") LocalDate toDate);
	
}
