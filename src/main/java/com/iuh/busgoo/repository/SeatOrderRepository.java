package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.busgoo.entity.SeatOrder;

public interface SeatOrderRepository extends JpaRepository<SeatOrder, Long>{

	List<SeatOrder> findByTimeTable(Long timeTableId);

	Long countByIsAvailable(boolean b);

}
