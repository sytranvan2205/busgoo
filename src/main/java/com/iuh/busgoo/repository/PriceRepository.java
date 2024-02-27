package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
	List<Price> findByRouteIdAndTypeBusId(Long routeId, Long typeBusId);
}
