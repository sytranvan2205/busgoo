package com.iuh.busgoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>{
	List<Seat> findByBusIdAndStatus(Long busId, Integer status);
}
