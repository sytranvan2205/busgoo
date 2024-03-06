package com.iuh.busgoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iuh.busgoo.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>{

}
