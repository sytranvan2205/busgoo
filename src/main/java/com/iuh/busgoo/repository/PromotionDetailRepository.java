package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.PromotionDetail;

@Repository
public interface PromotionDetailRepository extends JpaRepository<PromotionDetail, Long>{
	List<PromotionDetail> findByStatus(Integer status);

	PromotionDetail findByPromotionLineIdAndStatus(Long promotionLineId,Integer status);

	PromotionDetail findByIdAndStatus(Long promotionDetailId, Integer i);
}
