package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.SeatOrder;

@Repository
public interface SeatOrderRepository extends JpaRepository<SeatOrder, Long>{

	List<SeatOrder> findByTimeTableId(Long timeTableId);

	Long countByIsAvailableAndTimeTableId(boolean b,Long timeTableId);

}
