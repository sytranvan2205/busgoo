package com.iuh.busgoo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.dto.PromotionDTO;
import com.iuh.busgoo.entity.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Long>{
	List<PromotionDetail> findByStatus(Integer status);

	PromotionDetail findByPromotionLineIdAndStatus(Long promotionLineId,Integer status);

	PromotionDetail findByIdAndStatus(Long promotionDetailId, Integer i);

	@Query("select new com.iuh.busgoo.dto.PromotionDTO(p.code as promotionCode, pl.lineName as promotionLineName, pl.promotionType as promotionType, pd.discount as discount, pd.conditionApply as conditionApply, pd.maxDiscount as maxDiscount) "
			+ "from PromotionDetail pd "
			+ "INNER JOIN pd.promotionLine pl "
			+ "INNER JOIN pl.promotion p "
			+ "WHERE pl.status = 1 "
			+ "and pd.status =1 "
			+ "and p.status = 1 "
			+ "and pl.fromDate <= :currentDate "
			+ "and pl.toDate >= :currentDate")
	List<PromotionDTO> findByCurrentDate(LocalDate currentDate);
}
