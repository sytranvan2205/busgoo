package com.iuh.busgoo.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Promotion;
import com.iuh.busgoo.entity.PromotionLine;


@Repository
public interface PromotionLineRepository extends JpaRepository<PromotionLine, Long> {
	
	@Query("select pl from PromotionLine pl "
			+ "join Promotion p on pl.promotion.id = p.id "
			+ "where p.code = :promotionCode")
	List<PromotionLine> findByPromotionCode(@Param("promotionCode") String code);
	
	@Query("SELECT p FROM PromotionLine p WHERE (:startDate IS NULL OR p.fromDate >= :startDate) AND (:endDate IS NULL OR p.toDate <= :endDate)")
    List<PromotionLine> findPromotionsBetweenDates(Date startDate, Date endDate);
	
	List<PromotionLine> findByPromotionId(Long promotionId);

	PromotionLine findByIdAndStatus(Long promotionLineId, int i);
	
	@Query(value="select pl.* from promotion_line pl "
			+ "INNER JOIN promotion p on pl.promotion_id = p.id and p.status =1 "
			+ "INNER JOIN promotion_detail pd on pl.id = pd.promotion_line_id and pd.status =1 "
			+ "Where pl.from_date <= :currDate "
			+ "and pl.to_date >= :currDate "
			+ "and pd.condition_apply <= :total "
			+ "and pl.status =1 ", nativeQuery = true)
	List<PromotionLine> findPromotionLineByCondition(LocalDate currDate, BigDecimal total );

	List<PromotionLine> findByPromotionIdAndStatus(Long promotionId, Integer i);

	@Query("select pl from PromotionLine pl where (:promotionCode is null or pl.code = :promotionCode) and :fromDate <= pl.fromDate and :toDate >= pl.toDate")
	List<PromotionLine> findPromotionLineForReport(String promotionCode, LocalDate fromDate, LocalDate toDate);
}
