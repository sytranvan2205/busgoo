package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.PriceDetail;

@Repository
public interface PriceDetailRepository extends JpaRepository<PriceDetail, Long>{
	List<PriceDetail> findByRouteCodeAndTypeBusCode(String roleCode, String typeBusCode);
	
	List<PriceDetail> findByRouteIdAndTypeBusId(Long routeId, Long typeBusId);
	
	List<PriceDetail> findByPriceId(Long priceId);
}
