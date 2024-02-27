package com.iuh.busgoo.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.PromotionLine;


@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Long> {
	
	@Query("select pl from PromotionLine pl "
			+ "join Promotion p on pl.promotion.id = p.id "
			+ "where p.code = :promotionCode")
	List<PromotionLine> findByPromotionCode(@Param("promotionCode") String code);
	
	@Query("SELECT p FROM PromotionLine p WHERE (:startDate IS NULL OR p.fromDate >= :startDate) AND (:endDate IS NULL OR p.toDate <= :endDate)")
    List<PromotionLine> findPromotionsBetweenDates(Date startDate, Date endDate);
}
