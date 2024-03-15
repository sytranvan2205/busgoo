package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iuh.busgoo.entity.Bus;

import java.util.List;

@Repository
public interface BusRepository extends JpaRepository<Bus, Long>{
    List<Bus> findByStatus(Integer status);
}
